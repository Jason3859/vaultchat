package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.dto.AddUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.dto.UserTokenDto;
import dev.jason.project.spring.vc_server.users.UserDbService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused") // For IntelliJ IDEA Community Edition
@RestController
public class VcRestController {

    private final UserDbService userDbService;

    public VcRestController(UserDbService userDbService) {
        this.userDbService = userDbService;
    }

    @GetMapping
    public String home() {
        return "Hello, World!";
    }


    @PostMapping("/send")
    public ResultDto send(@RequestBody Message body) {

        ResultDto resultDto;

        if (body.getText().isBlank()) {
            return new ResultDto(ResultDto.Result.MessageTextBlank);
        }

        boolean isUserBlocked = false;
        try {
            User to = userDbService.getUserByUid(body.getTo());
            isUserBlocked = Arrays.asList(to.blocklist()).contains(body.getFrom());
        } catch (NullPointerException ignored) {}

        if (isUserBlocked) {
            return new ResultDto(ResultDto.Result.BlockedByUser);
        }

        try {
            userDbService.addConnection(body.getFrom(), body.getTo());
            userDbService.addConnection(body.getTo(), body.getFrom());

            String token = userDbService.getUserFcmTokenByUid(body.getTo());
            FirebaseMessaging.getInstance().send(body.toMessage(token));

            resultDto = new ResultDto(ResultDto.Result.Success);
        } catch (FirebaseMessagingException e) {
            Logger.write(e);
            resultDto = new ResultDto(ResultDto.Result.InternalServerError);
        } catch (NullPointerException e) {
            resultDto = new ResultDto(ResultDto.Result.UserNotFound);
        }

        return resultDto;
    }

    @PostMapping("/update-token")
    public ResultDto updateToken(@RequestBody UserTokenDto userTokenDto) {
        ResultDto resultDto;

        try {
            userDbService.updateUserFcmToken(userTokenDto.uid(), userTokenDto.fcmToken());
            resultDto = new ResultDto(ResultDto.Result.Success);
        } catch (NullPointerException e) {
            resultDto = new ResultDto(ResultDto.Result.UserNotFound);
        }

        return resultDto;
    }

    @PostMapping("/add-user")
    public ResultDto addUser(
        @RequestBody AddUserDto userDto,
        @RequestParam(value = "is_test_user", required = false) boolean isTestUser
    ) {
        User user = userDbService.getUserByUid(userDto.uid());

        if (user == null) {
            userDbService.saveUser(userDto.toDbUser(isTestUser));
            return new ResultDto(ResultDto.Result.Success);
        }

        return new ResultDto(ResultDto.Result.UserAlreadyExists);
    }

    @PostMapping("/block-user")
    public ResultDto blockUser(@RequestParam("uid") String uid, @RequestParam("uid_to_block") String uidToBlock) {
        User user = userDbService.getUserByUid(uid);
        User userToBlock = userDbService.getUserByUid(uidToBlock);

        if (user == null || userToBlock == null) {
            return new ResultDto(ResultDto.Result.UserDoesntExist);
        }

        if (uid.equals(uidToBlock)) {
            return new ResultDto(ResultDto.Result.SelfBlock);
        }

        try {
            boolean isAlreadyBlocked = Arrays.asList(user.blocklist()).contains(uidToBlock);

            if (isAlreadyBlocked) {
                return new ResultDto(ResultDto.Result.AlreadyBlocked);
            }
        } catch (NullPointerException ignored) {}

        userDbService.addBlocklist(uid, uidToBlock);
        userDbService.addBlocklist(uidToBlock, uid);
        return new ResultDto(ResultDto.Result.Success);
    }

    @PostMapping("/unblock-user")
    public ResultDto unblockUser(@RequestParam("uid") String uid, @RequestParam("uid_to_unblock") String uidToUnblock) {
        User user = userDbService.getUserByUid(uid);
        User userToUnblock = userDbService.getUserByUid(uidToUnblock);

        if (user == null || userToUnblock == null) {
            return new ResultDto(ResultDto.Result.UserDoesntExist);
        }

        if (uid.equals(uidToUnblock)) {
            return new ResultDto(ResultDto.Result.SelfUnblock);
        }

        try {
            userDbService.unblock(uid, uidToUnblock);
            return new ResultDto(ResultDto.Result.Success);
        } catch (NullPointerException e) {
            return new ResultDto(ResultDto.Result.UserNotBlocked);
        }
    }

    @GetMapping("/get-blocked-users")
    public List<UserDto> getBlockedUsers(@RequestParam("uid") String uid) {
        try {
            List<String> blockedUserUids = userDbService.getBlockedUserUidsByUserUid(uid);

            return blockedUserUids.stream()
                .map(userDbService::getUserByUid)
                .map(this::getFromDomainUser)
                .toList();
        } catch (NullPointerException e) {
            return new ArrayList<>(List.of());
        }
    }

    @GetMapping("/search-users/{name}")
    public List<UserDto> searchUsers(@PathVariable("name") String name, @RequestParam("from") String from) {
        List<UserDto> requiredUsers = new ArrayList<>(List.of());

        userDbService.getAllUsersByDisplayName(name).stream()
            .map(this::getFromDomainUser)
            .filter(user -> !user.uid().equals(from))
            .forEach(requiredUsers::add);

        return requiredUsers;
    }

    @GetMapping("/get-connections/{uid}")
    public List<UserDto> getConnections(@PathVariable("uid") String uid) {
        try {
            return Arrays.stream(userDbService.getUserByUid(uid).connections())
                .map(userDbService::getUserByUid)
                .map(this::getFromDomainUser)
                .toList();
        } catch (NullPointerException e) {
            return new ArrayList<>(List.of());
        }
    }

    @GetMapping("/stacktrace")
    public String stacktrace() {
        File logFile = Logger.getLogFile();
        StringBuilder stringBuilder = new StringBuilder();

        try (Scanner scanner = new Scanner(logFile)) {
            while (scanner.hasNext()) {
                stringBuilder.append(String.format("%s\n", scanner.nextLine()));
            }

            return new String(stringBuilder);
        } catch (FileNotFoundException ignored) {
            return "Log file not found";
        }
    }

    private UserDto getFromDomainUser(User user) {
        return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl());
    }
}
