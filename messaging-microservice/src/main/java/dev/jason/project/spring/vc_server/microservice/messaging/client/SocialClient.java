package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Message;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "social-microservice", url = Endpoints.SOCIAL_SERVICE_URL, configuration = FeignConfig.class)
public interface SocialClient {

    @PatchMapping(Endpoints.SOCIAL_CONNECT)
    void connect(@RequestParam("from_uid") String uid1, @RequestParam("other_uid") String uid2);

    @PatchMapping(Endpoints.SOCIAL_ADD_MESSAGE_TO_QUEUE)
    void addMessageToQueue(@RequestParam String uid, @RequestBody Message message);

    @GetMapping(Endpoints.SOCIAL_GET_CONNECTIONS)
	List<UserDto> getConnections(@RequestParam String uid);
    
    @GetMapping(Endpoints.SOCIAL_IS_USER_BLOCKED)
    boolean isUserBlocked(@RequestParam String uid1, @RequestParam String uid2);
}
