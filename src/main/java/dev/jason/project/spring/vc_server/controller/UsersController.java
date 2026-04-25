package dev.jason.project.spring.vc_server.controller;

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

import dev.jason.project.spring.vc_server.data.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.data.dto.ResultDto;
import dev.jason.project.spring.vc_server.data.dto.UserDto;
import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.service.UserHeartbeatService;
import dev.jason.project.spring.vc_server.domain.service.UserService;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserHeartbeatService userHeartbeatService;

    @PostMapping("/register")
    public ResultDto registerUser(@RequestBody RegisterUserDto userDto) {
        try {
            userService.saveUser(userDto.toDomainUser());
            return new ResultDto(Result.Success);
        } catch (DeviceAlreadyExistsException e) {
            return new ResultDto(Result.UserAlreadyExists);
        }
    }

    @GetMapping("/search")
    public ResultDto searchUsers(@RequestParam String name, @RequestParam String from) {
        User user;
        List<UserDto> requiredUsers = new ArrayList<>(List.of());

        try {
            user = userService.getUserOrThrow(from);
        } catch (UserNotFoundException e) {
            return new ResultDto(Result.UserNotFound);
        }

        userService.getAllUsersByDisplayName(name).stream()
            .filter(u -> !u.uid().equals(from))
            .filter(u -> !user.blocklist().contains(u.uid()))
            .filter(u -> !user.connections().contains(u.uid()))
            .map(UserDto::fromDomainUser)
            .forEach(requiredUsers::add);

        return requiredUsers.isEmpty() ? new ResultDto(Result.NoUsersFound)
            : new ResultDto(Result.Success, requiredUsers);

    }

    @PutMapping("/update-status")
    public ResultDto updateUserStatus(@RequestParam String uid, @RequestParam User.Status status) {
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
            userHeartbeatService.updateHeartbeat(uid);
            return new ResultDto(Result.Success);
        } catch (UserNotFoundException e) {
            return ResultDto.fromVcException(e);
        }
    }
}
