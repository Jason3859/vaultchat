package dev.jason.project.spring.vc_server.social_microservice.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;

@FeignClient(name = "messaging-microservice", url = Endpoints.MESSAGING_SERVICE_URL + "/" + Endpoints.MESSAGING)
public interface MessagingClient {

	@PostMapping
	public void sendData(@RequestParam String token, @RequestParam String type, @RequestBody Map<String, String> data);
}
