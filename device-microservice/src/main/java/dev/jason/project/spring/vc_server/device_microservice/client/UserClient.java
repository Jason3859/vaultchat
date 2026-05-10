package dev.jason.project.spring.vc_server.device_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-microservice", url = "http://localhost:9003/user")
public interface UserClient {

	@GetMapping("/get-user-by-uid")
	Object getUserByUid(@RequestParam String uid);
}
