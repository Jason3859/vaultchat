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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused") // For IntelliJ IDEA
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

        if (body.text().isBlank()) {
            return new ResultDto(ResultDto.Result.ClientSideError, "message text is blank");
        }

        try {
            userDbService.addConnection(body.from(), body.to());
            userDbService.addConnection(body.to(), body.from());

            String token = userDbService.getUserFcmTokenByUid(body.to());
            FirebaseMessaging.getInstance().send(body.toMessage(token));
            return new ResultDto(ResultDto.Result.Success, null);
        } catch (FirebaseMessagingException e) {
            Logger.write(e);
            return new ResultDto(ResultDto.Result.InternalError, e.getMessage());
        } catch (NullPointerException e) {
            return new ResultDto(ResultDto.Result.ClientSideError, "User not found");
        }
    }

    @PostMapping("/update-token")
    public ResultDto updateToken(@RequestBody UserTokenDto userTokenDto) {
        try {
            userDbService.updateUserFcmToken(userTokenDto.uid(), userTokenDto.fcmToken());
            return new ResultDto(ResultDto.Result.Success, null);
        } catch (NullPointerException e) {
            return new ResultDto(ResultDto.Result.ClientSideError, "User not found");
        }
    }

    @PostMapping("/add-user")
    public ResultDto addUser(@RequestBody AddUserDto userDto) {
        User user = userDbService.getUserByUid(userDto.uid());

        if (user == null) {
            userDbService.saveUser(userDto.toDbUser(null));
            return new ResultDto(ResultDto.Result.Success, null);
        }

        return new ResultDto(ResultDto.Result.UnknownError, "user already exists");
    }

    @GetMapping("/search-users/{name}")
    public List<UserDto> searchUsers(@PathVariable("name") String name, @RequestParam("from") String from) {
        List<UserDto> requiredUsers = new ArrayList<>(List.of());

        userDbService.getAllUsersByDisplayName(name).stream()
                .map(user -> new UserDto(user.uid(), user.displayName(), user.profilePictureUrl()))
                .filter(user -> !user.uid().equals(from))
                .forEach(requiredUsers::add);

        return requiredUsers;
    }

    @GetMapping("/get-connections/{uid}")
    public List<UserDto> getConnections(@PathVariable("uid") String uid) {
        return Arrays.stream(userDbService.getUserDbEntityByUid(uid).connections())
                .map(userDbService::getUserDbEntityByUid)
                .map(user -> new UserDto(user.uid(), user.displayName(), user.profilePictureUrl()))
                .toList();
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
}
