package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-microservice", url = Endpoints.USER_SERVICE_URL)
public interface UserClient {

    @GetMapping(Endpoints.USER_GET_USER_BY_UID)
    UserDto getUserById(@RequestParam String uid);
}
