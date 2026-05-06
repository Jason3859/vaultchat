package dev.jason.project.spring.vc_server.social_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.dto.UserDto;

@FeignClient(name = "user-microservice", url = "http://localhost:9003")
public interface UserClient {

	@GetMapping("/user/get-user-by-id")
	UserDto getUserById(@RequestParam String uid);
}
