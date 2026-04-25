package dev.jason.project.spring.vc_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.data.dto.ResultDto;
import dev.jason.project.spring.vc_server.data.dto.UserDto;
import dev.jason.project.spring.vc_server.domain.exception.NoUsersBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserAlreadyBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotBlockedException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.service.UserSocialConnectionsService;

@RestController
@RequestMapping("/user/social")
public class UserSocialConnectionsController {
	
	@Autowired
	private UserSocialConnectionsService userSocialConnectionsService;
	
	@PostMapping("/block")
    public ResultDto blockUser(@RequestParam String uid, @RequestParam("uid_to_block") String uidToBlock) {
        try {
            User user = userSocialConnectionsService.getUserOrThrow(uid);
            User userToBlock = userSocialConnectionsService.getUserOrThrow(uidToBlock);

            if (uid.equals(userToBlock.uid())) {
                return new ResultDto(Result.SelfBlock);
            }

            if (user.blocklist().contains(userToBlock.uid())) {
            	return new ResultDto(Result.UserAlreadyBlocked);
            }

            userSocialConnectionsService.block(uid, uidToBlock);
            return new ResultDto(Result.Success);
        } catch (UserNotFoundException | UserAlreadyBlockedException e) {
            return ResultDto.fromVcException(e);
        }
    }

    @PostMapping("/unblock")
    public ResultDto unblockUser(@RequestParam String uid, @RequestParam("uid_to_unblock") String uidToUnblock) {
        if (uid.equals(uidToUnblock)) {
            return new ResultDto(Result.SelfUnblock);
        }

        try {
            userSocialConnectionsService.unblock(uid, uidToUnblock);
            return new ResultDto(Result.Success);
        } catch (UserNotFoundException | NoUsersBlockedException | UserNotBlockedException e) {
            return ResultDto.fromVcException(e);
        }
    }
    
    @GetMapping("/get-connections")
    public ResultDto getConnections(@RequestParam String uid) {
        try {
            User user = userSocialConnectionsService.getUserOrThrow(uid);

            Object data = user.connections().stream()
                .map(userSocialConnectionsService::getUserByUid)
                .map(UserDto::fromDomainUser)
                .toList();

            return new ResultDto(Result.Success, data);
        } catch (NullPointerException e) {
            return new ResultDto(Result.NoUsersFound);
        } catch (UserNotFoundException ignored) {
            return new ResultDto(Result.UserNotFound);
        }
    }

    @GetMapping("/get-blocked-users")
    public ResultDto getBlockedUsers(@RequestParam String uid) {
        try {
            User user = userSocialConnectionsService.getUserOrThrow(uid);

            if (user.blocklist() == null || user.blocklist().isEmpty()) {
                return new ResultDto(Result.NoBlockedUsers);
            }

            Object data = user.blocklist().stream()
                .map(userSocialConnectionsService::getUserByUid)
                .map(UserDto::fromDomainUser)
                .toList();

            return new ResultDto(Result.Success, data);
        } catch (UserNotFoundException e) {
            return new ResultDto(Result.UserNotFound);
        }
    }
}
