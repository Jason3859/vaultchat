package dev.jason.project.spring.vc_server;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;

@FeignClient(name = "social-microservice", url = Endpoints.SOCIAL_SERVICE_URL)
public interface SocialClient {

	@PostMapping(Endpoints.SOCIAL_REGISTER)
	void registerUser(@RequestParam String uid);
}
