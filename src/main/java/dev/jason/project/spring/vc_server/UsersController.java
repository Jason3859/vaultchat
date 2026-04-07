package dev.jason.project.spring.vc_server;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import dev.jason.project.spring.vc_server.domain.Logger;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.exception.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.ResultDto.Result;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<String> usersOnline = new ArrayList<>(List.of());

    @PostMapping("/register")
    public ResultDto registerUser(@RequestBody RegisterUserDto userDto) {
        userService.saveUser(userDto.toDomainUser());
        return new ResultDto(ResultDto.Result.Success);
    }

    @PostMapping("/mark-online")
    public ResultDto markOnline(@RequestParam String uid) {
        try {
            userService.verifyUserOrThrow(uid);
            usersOnline.add(uid);
            notifyUsersAboutOnlineUsers();
            return new ResultDto(Result.Success);
        } catch (Exception e) {
            return new ResultDto(Result.UserNotFound);
        }
    }

    @PostMapping("/mark-offline")
    public ResultDto markOffline(@RequestParam String uid) {
        try {
            userService.verifyUserOrThrow(uid);
            if (usersOnline.contains(uid)) {
                usersOnline.remove(uid);
                notifyUsersAboutOnlineUsers();
                return new ResultDto(Result.Success);
            } else {
                return new ResultDto(Result.NotOnline);
            }
        } catch (UserNotFoundException e) {
            return new ResultDto(Result.UserNotFound);
        }
    }

    @PostMapping("/block")
    public ResultDto blockUser(@RequestParam String uid, @RequestParam("uid_to_block") String uidToBlock) {
        try {
            User user = userService.getUserOrThrow(uid);
            User userToBlock = userService.getUserOrThrow(uidToBlock);

            if (uid.equals(userToBlock.uid())) {
                return new ResultDto(ResultDto.Result.SelfBlock);
            }

            if (user.blocklist() != null) {
                if (user.blocklist().contains(userToBlock.uid())) {
                    return new ResultDto(ResultDto.Result.AlreadyBlocked);
                }
            }

            userService.addBlocklist(uid, uidToBlock);
            userService.addBlocklist(uidToBlock, uid);
            return new ResultDto(ResultDto.Result.Success);
        } catch (UserNotFoundException e) {
            return new ResultDto(ResultDto.Result.UserNotFound);
        }
    }

    @PostMapping("/unblock")
    public ResultDto unblockUser(@RequestParam String uid, @RequestParam("uid_to_unblock") String uidToUnblock) {
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

    @GetMapping("/search")
    public ResultDto searchUsers(@RequestParam String name, @RequestParam String from) {
        List<UserDto> requiredUsers = new ArrayList<>(List.of());

        userService.getAllUsersByDisplayName(name).stream()
            .map(UserDto::fromDomainUser)
            .filter(user -> !user.uid().equals(from))
            .forEach(requiredUsers::add);

        return requiredUsers.isEmpty() ? new ResultDto(ResultDto.Result.NoUsersFound)
            : new ResultDto(ResultDto.Result.Success, requiredUsers);

    }

    @GetMapping("/get-connections")
    public ResultDto getConnections(@RequestParam String uid) {
        try {
            User user = userService.getUserOrThrow(uid);

            Object data = user.connections().stream()
                .map(userService::getUserByUid)
                .map(UserDto::fromDomainUser)
                .toList();

            return new ResultDto(ResultDto.Result.Success, data);
        } catch (NullPointerException e) {
            return new ResultDto(ResultDto.Result.NoUsersFound);
        } catch (UserNotFoundException ignored) {
            return new ResultDto(Result.UserNotFound);
        }
    }

    private void notifyUsersAboutOnlineUsers() {
        List<User> users = userService.getAllUsers();

        users.forEach(user -> {
            List<String> list = new ArrayList<>(List.of());

            user.connections().stream()
                .filter(usersOnline::contains)
                .forEach(list::add);

            user.fcmTokens().forEach(fcmToken -> {
                try {
                    Message message = Message.builder()
                        .setToken(fcmToken.token())
                        .putData("online_users", String.valueOf(true))
                        .putData("users_online", objectMapper.writeValueAsString(list))
                        .build();

                    FirebaseMessaging.getInstance().send(message);
                } catch (FirebaseMessagingException e) {
                    Logger.write(e);
                }
            });
        });
    }
}
