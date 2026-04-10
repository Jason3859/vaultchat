package dev.jason.project.spring.vc_server;

import dev.jason.project.spring.vc_server.domain.Device;
import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;
import dev.jason.project.spring.vc_server.domain.exception.UserAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.ResultDto.Result;
import dev.jason.project.spring.vc_server.dto.UserDto;
import dev.jason.project.spring.vc_server.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        } catch (UserAlreadyExistsException e) {
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

    @RestController
    @RequestMapping("/devices")
    public static class DeviceController {

        @Autowired
        private UserService userService;

        @PostMapping("/add")
        public ResultDto addDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
            try {
                userService.addDevice(uid, device.toDomainModel(LocalDateTime.now()));
                return new ResultDto(Result.Success);
            } catch (VcException e) {
                return ResultDto.fromVcException(e);
            }
        }

        @GetMapping("/my-devices")
        public ResultDto getMyDevices(@RequestParam String uid) {
            try {
                List<Device> devices = userService.getUserDevicesByUid(uid);
                return new ResultDto(Result.Success, devices);
            } catch (VcException e) {
                return ResultDto.fromVcException(e);
            }
        }

        @DeleteMapping("/delete")
        public ResultDto deleteDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
            try {
                userService.deleteDevice(uid, device.toDomainModel(null));
                return new ResultDto(Result.Success);
            } catch (VcException e) {
                return ResultDto.fromVcException(e);
            }
        }
    }
}
