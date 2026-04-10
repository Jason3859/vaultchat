package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import dev.jason.project.spring.vc_server.domain.Device;
import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.Message;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/send")
public class SendController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResultDto send(@RequestBody Message body) {
        try {
            User from = userService.getUserOrThrow(body.from());
            User to = userService.getUserOrThrow(body.to());

            if (body.text().isBlank()) {
                return new ResultDto(ResultDto.Result.MessageTextBlank);
            }

            if (to.blocklist() != null) {
                if (to.blocklist().contains(from.uid())) { // if user is blocked
                    return new ResultDto(ResultDto.Result.BlockedByUser);
                }
            }
            userService.addConnection(from, to);
            userService.addConnection(to, from);

            List<Device> devices = userService.getUserDevicesByUid(body.to());

            for (Device device : devices) {
                FirebaseMessaging.getInstance().send(body.toFirebaseMessage(device.fcmToken()));
            }

            return new ResultDto(ResultDto.Result.Success);
        } catch (FirebaseMessagingException | NullPointerException e) {
            Logger.write(e);
            return new ResultDto(ResultDto.Result.InternalServerError);
        } catch (UserNotFoundException e) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }
    }

    @PostMapping("/notify-fcm-token-deletion")
    public void notifyFcmTokenDeletion(@RequestParam String token) {
        try {
            FirebaseMessaging.getInstance().send(
                com.google.firebase.messaging.Message.builder()
                    .setToken(token)
                    .putData("token_deleted", "reason: inactivity")
                    .build()
            );
        } catch (FirebaseMessagingException e) {
            Logger.write(e);
        }
    }
}
