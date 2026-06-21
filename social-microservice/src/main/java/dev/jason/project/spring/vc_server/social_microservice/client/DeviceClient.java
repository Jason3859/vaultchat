package dev.jason.project.spring.vc_server.social_microservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;

@FeignClient(name = "device-microservice", url = Endpoints.DEVICE_SERVICE_URL + "/" + Endpoints.DEVICE)
public interface DeviceClient {

	@GetMapping(Endpoints.GET_DEVICES_BY_OWNER)
	List<DeviceDto> getDevices(@RequestParam String uid);
}
