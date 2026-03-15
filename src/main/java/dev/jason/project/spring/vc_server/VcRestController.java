package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.dto.AddUserDto;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.dto.UserTokenDto;
import dev.jason.project.spring.vc_server.users.UserDbService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public void send(@RequestBody Message body) {

        if (body.text().isBlank()) {
            return;
        }

        try {
            userDbService.addConnection(body.from(), body.to());

            String token = userDbService.getUserFcmTokenByUid(body.to());

            FirebaseMessaging.getInstance().send(body.toMessage(token));
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/update-token")
    public void updateToken(@RequestBody UserTokenDto userTokenDto) {
        userDbService.updateUserFcmToken(userTokenDto.uid(), userTokenDto.fcmToken());
    }

    @PostMapping("/add-user")
    public void addUser(@RequestBody AddUserDto userDto) {
        userDbService.saveUser(userDto.toDbUser(new String[0]));
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
}
