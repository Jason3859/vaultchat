package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import dev.jason.project.spring.vc_server.domain.*;
import dev.jason.project.spring.vc_server.domain.exception.*;
import dev.jason.project.spring.vc_server.dto.AddUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.dto.UserTokenDto;
import dev.jason.project.spring.vc_server.users.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@RestController
public class VcRestController {

    private final UserService userService;

    public VcRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("/send")
    public ResultDto send(@RequestBody Message body) {

        User from, to;

        try {
            from = userService.getUserOrThrow(body.from());
            to = userService.getUserOrThrow(body.to());
        } catch (UserNotFoundException e) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }

        if (body.text().isBlank()) {
            return new ResultDto(ResultDto.Result.MessageTextBlank);
        }

        boolean isUserBlocked = false;
        try {
            isUserBlocked = Arrays.asList(to.blocklist()).contains(body.from());
        } catch (NullPointerException ignored) {
        }

        if (isUserBlocked) {
            return new ResultDto(ResultDto.Result.BlockedByUser);
        }

        try {
            userService.addConnection(from, to);
            userService.addConnection(to, from);

            String token = userService.getUserFcmTokenByUid(body.to());
            FirebaseMessaging.getInstance().send(body.toMessage(token));

            return new ResultDto(ResultDto.Result.Success);
        } catch (FirebaseMessagingException | NullPointerException e) {
            Logger.write(e);
            return new ResultDto(ResultDto.Result.InternalServerError);
        } catch (UserNotFoundException e) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }
    }

    @PostMapping("/update-token")
    public ResultDto updateToken(@RequestBody UserTokenDto userTokenDto) {
        ResultDto resultDto;

        try {
            userService.updateUserFcmToken(userTokenDto.uid(), userTokenDto.fcmToken());
            resultDto = new ResultDto(ResultDto.Result.Success);
        } catch (UserNotFoundException e) {
            resultDto = new ResultDto(ResultDto.Result.UserNotFound);
        }

        return resultDto;
    }

    @PostMapping("/add-user")
    public ResultDto addUser(
        @RequestBody AddUserDto userDto
    ) {
        userService.saveUser(userDto.toDbUser());
        return new ResultDto(ResultDto.Result.Success);
    }

    @PostMapping("/block-user")
    public ResultDto blockUser(@RequestParam("uid") String uid, @RequestParam("uid_to_block") String uidToBlock) {
        User user = userService.getUserByUid(uid);
        User userToBlock = userService.getUserByUid(uidToBlock);

        if (user == null || userToBlock == null) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }

        if (uid.equals(uidToBlock)) {
            return new ResultDto(ResultDto.Result.SelfBlock);
        }

        try {
            boolean isAlreadyBlocked = Arrays.asList(user.blocklist()).contains(uidToBlock);

            if (isAlreadyBlocked) {
                return new ResultDto(ResultDto.Result.AlreadyBlocked);
            }
        } catch (NullPointerException ignored) {
        }

        userService.addBlocklist(uid, uidToBlock);
        userService.addBlocklist(uidToBlock, uid);
        return new ResultDto(ResultDto.Result.Success);
    }

    @PostMapping("/unblock-user")
    public ResultDto unblockUser(@RequestParam("uid") String uid, @RequestParam("uid_to_unblock") String uidToUnblock) {
        if (uid.equals(uidToUnblock)) {
            return new ResultDto(ResultDto.Result.SelfUnblock);
        }

        try {
            userService.unblock(uid, uidToUnblock);
            return new ResultDto(ResultDto.Result.Success);
        } catch (VcException e) {
            return switch (e) {
                case UserNotFoundException ignored -> new ResultDto(ResultDto.Result.UserNotFound);
                case UserNotBlockedException ignored -> new ResultDto(ResultDto.Result.UserNotBlocked);
                case NoUsersBlockedException ignored -> new ResultDto(ResultDto.Result.UserNotBlocked);
                default -> {
                    Logger.write(e);
                    yield new ResultDto(ResultDto.Result.InternalServerError);
                }
            };
        }
    }

    @GetMapping("/get-blocked-users")
    public ResultDto getBlockedUsers(@RequestParam("uid") String uid) {
        List<String> blockedUserUids;

        try {
            blockedUserUids = userService.getBlockedUserUidsByUserUid(uid);
        } catch (NullPointerException ignored) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }

        try {
            Object data = blockedUserUids.stream()
                .map(userService::getUserByUid)
                .map(this::getFromDomainUser)
                .toList();

            return new ResultDto(ResultDto.Result.Success, data);
        } catch (NullPointerException ignored) {
            return new ResultDto(ResultDto.Result.NoBlockedUsers);
        }
    }

    @GetMapping("/search-users")
    public ResultDto searchUsers(@RequestParam("name") String name, @RequestParam("from") String from) {
        List<UserDto> requiredUsers = new ArrayList<>(List.of());

        userService.getAllUsersByDisplayName(name).stream()
            .map(this::getFromDomainUser)
            .filter(user -> !user.uid().equals(from))
            .forEach(requiredUsers::add);

        return requiredUsers.isEmpty()
            ? new ResultDto(ResultDto.Result.NoUsersFound)
            : new ResultDto(ResultDto.Result.Success, requiredUsers);

    }

    @GetMapping("/get-connections")
    public ResultDto getConnections(@RequestParam("uid") String uid) {
        User user;

        try {
            user = userService.getUserOrThrow(uid);
        } catch (UserNotFoundException e) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }

        try {
            Object data = Arrays.stream(user.connections())
                .map(userService::getUserByUid)
                .map(this::getFromDomainUser)
                .toList();

            return new ResultDto(ResultDto.Result.Success, data);
        } catch (NullPointerException e) {
            return new ResultDto(ResultDto.Result.NoUsersFound);
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
