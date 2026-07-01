package dev.jason.project.spring.vc_server.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.UserDto;

@FeignClient(name = "user-microservice", url = Endpoints.USER_SERVICE_URL)
public interface UserClient {

	@PostMapping(Endpoints.USER_REGISTER)
	void registerUser(@RequestBody UserDto dto);
}
