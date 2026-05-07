package dev.jason.project.spring.vc_server.user_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;

@FeignClient(name = "device-microservice", url = "http://localhost:9000")
public interface DeviceClient {

	@PostMapping("/device/add")
	DeviceDto register(@RequestBody DeviceDto device);
}
