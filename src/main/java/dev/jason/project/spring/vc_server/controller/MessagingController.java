package dev.jason.project.spring.vc_server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.data.dto.ResultDto;
import dev.jason.project.spring.vc_server.domain.exception.BlockedByUserException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.UsersAlreadyConnectedException;
import dev.jason.project.spring.vc_server.domain.model.Device;
import dev.jason.project.spring.vc_server.domain.model.Message;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.model.User;
import dev.jason.project.spring.vc_server.domain.repo.messaging.MessagingRepository;
import dev.jason.project.spring.vc_server.domain.service.UserSocialConnectionsService;

@RestController
@RequestMapping("/messaging")
public class MessagingController {

    @Autowired
    private UserSocialConnectionsService userSocialConnectionsService;

    @Autowired
    private MessagingRepository messagingRepository;

    @PostMapping("/send")
    public ResultDto send(@RequestBody Message body) {
        try {
            User from = userSocialConnectionsService.getUserOrThrow(body.from());
            User to = userSocialConnectionsService.getUserOrThrow(body.to());

            if (body.text().isBlank()) {
                return new ResultDto(Result.MessageTextBlank);
            }

            if (to.blocklist() != null) {
                if (to.blocklist().contains(from.uid())) { // if user is blocked
                    return new ResultDto(Result.BlockedByUser);
                }
            }
            userSocialConnectionsService.addConnection(from, to);
            userSocialConnectionsService.addConnection(to, from);

            List<Device> devices = userSocialConnectionsService.getUserByUid(body.to()).devices();

            if (!devices.isEmpty()) {
                for (Device device : devices) {
                    Result result = messagingRepository.sendMessage(body, device);

                    if (result != Result.Success) {
                        return new ResultDto(result);
                    }
                }
            } else {
                userSocialConnectionsService.addMessageToQueue(to.uid(), body);
            }

            return new ResultDto(Result.Success);
        } catch (UserNotFoundException | UsersAlreadyConnectedException | BlockedByUserException e) {
            return ResultDto.fromVcException(e);
        }
    }
}
