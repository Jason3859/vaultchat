package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Message;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "social-microservice", url = "http://localhost:9002", configuration = FeignConfig.class)
public interface SocialClient {

    @PatchMapping("/social/connect")
    void connect(@RequestParam("from_uid") String uid1, @RequestParam("other_uid") String uid2);

    @PatchMapping("/social/add-message-to-queue")
    void addMessageToQueue(@RequestParam String uid, @RequestBody Message message);

    @GetMapping("/social/get-connections")
	List<UserDto> getConnections(@RequestParam String uid);
}
