package dev.jason.project.spring.vc_server.device_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;

@FeignClient(name = "user-microservice", url = Endpoints.USER_SERVICE_URL)
public interface UserClient {

	@GetMapping(Endpoints.USER_GET_USER_BY_UID)
	void getUserByUid(@RequestParam String uid);
}
