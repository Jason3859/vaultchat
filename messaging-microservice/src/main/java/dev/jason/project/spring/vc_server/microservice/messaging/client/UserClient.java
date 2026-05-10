package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-microservice", url = "http://localhost:9003")
public interface UserClient {

    @GetMapping("/user/get-user-by-uid")
    UserDto getUserById(@RequestParam String uid);
}
