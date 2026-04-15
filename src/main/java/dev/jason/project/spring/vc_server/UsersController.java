package dev.jason.project.spring.vc_server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;
import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.ResultDto.Result;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.service.UserService;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResultDto registerUser(@RequestBody RegisterUserDto userDto) {
        try {
            userService.saveUser(userDto.toDomainUser());
            return new ResultDto(Result.Success);
        } catch (DeviceAlreadyExistsException e) {
            return new ResultDto(Result.UserAlreadyExists);
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

            if (user.blocklist().contains(userToBlock.uid())) {
            	return new ResultDto(ResultDto.Result.AlreadyBlocked);
            }

            userService.block(uid, uidToBlock);
            userService.block(uidToBlock, uid);
            return new ResultDto(ResultDto.Result.Success);
        } catch (VcException e) {
            return ResultDto.fromVcException(e);
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
            return ResultDto.fromVcException(e);
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

    @GetMapping("/get-blocked-users")
    public ResultDto getBlockedUsers(@RequestParam String uid) {
        try {
            User user = userService.getUserOrThrow(uid);

            if (user.blocklist() == null || user.blocklist().isEmpty()) {
                return new ResultDto(Result.NoBlockedUsers);
            }

            Object data = user.blocklist().stream()
                .map(userService::getUserByUid)
                .map(UserDto::fromDomainUser)
                .toList();

            return new ResultDto(Result.Success, data);
        } catch (UserNotFoundException e) {
            return new ResultDto(Result.UserNotFound);
        }
    }

    @PutMapping("/update-status")
    public ResultDto updateUserStatus(@RequestParam String uid, @RequestParam UserStatus status) {
        try {
            userService.updateUserStatusAndNotify(uid, status);
            return new ResultDto(Result.Success);
        } catch (UserNotFoundException e) {
            return ResultDto.fromVcException(e);
        }
    }

    @PutMapping("/heartbeat")
    public ResultDto heartbeat(@RequestParam String uid) {
        try {
            userService.updateHeartbeat(uid);
            return new ResultDto(Result.Success);
        } catch (UserNotFoundException e) {
            return ResultDto.fromVcException(e);
        }
    }

    @PostMapping("/update-token")
    public ResultDto updateToken(@RequestParam String uid, @RequestParam String token, @RequestBody DeviceDto device) {
        try {
            userService.updateToken(uid, token, device.toDomainModel(LocalDateTime.now()));
            return new ResultDto(Result.Success);
        } catch (VcException e) {
            return ResultDto.fromVcException(e);
        }
    }
}
