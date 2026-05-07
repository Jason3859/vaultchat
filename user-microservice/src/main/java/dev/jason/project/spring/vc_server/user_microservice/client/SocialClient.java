package dev.jason.project.spring.vc_server.user_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "social-microservice", url = "http://localhost:9002")
public interface SocialClient {

	@PostMapping("/social/register")
	void register(@RequestParam String uid);
}
