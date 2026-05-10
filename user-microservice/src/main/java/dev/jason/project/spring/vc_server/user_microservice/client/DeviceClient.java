package dev.jason.project.spring.vc_server.user_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;

@FeignClient(name = "device-microservice", url = Endpoints.DEVICE_SERVICE_URL)
public interface DeviceClient {

	@PostMapping(Endpoints.DEVICE_ADD)
	DeviceDto register(@RequestBody DeviceDto device);
}
