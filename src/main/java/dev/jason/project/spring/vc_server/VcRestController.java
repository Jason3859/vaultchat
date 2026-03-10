package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.dto.AddUserDto;
import dev.jason.project.spring.vc_server.dto.UserTokenDto;
import dev.jason.project.spring.vc_server.user_dms.UserDmsService;
import dev.jason.project.spring.vc_server.users.DBUser;
import dev.jason.project.spring.vc_server.users.UserDbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VcRestController {

    private final UserDmsService userDmsService;
    private final UserDbService userDbService;

    public VcRestController(UserDmsService userDmsService, UserDbService userDbService) {
        this.userDmsService = userDmsService;
        this.userDbService = userDbService;
    }

    @GetMapping
    public String home() {
        return "Hello, World!";
    }
    

    @PostMapping("/send")
    public void send(@RequestBody Message body) {
        try {
            userDmsService.addUserDm(body.from(), body.to());
            FirebaseMessaging.getInstance().send(body.toMessage());
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
        userDbService.saveUser(userDto.toDbUser());
    }

    @GetMapping("/get-users/{name}")
    public List<User> getUsers(@RequestParam String name) {
        return userDbService.getUserByDisplayName(name).stream()
                .map(DBUser::toDomainUser)
                .collect(Collectors.toList());
    }
}
