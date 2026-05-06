package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "device-microservice", url = "http://localhost:9001")
public interface DeviceClient {

    @GetMapping("/device/get-devices-by-owner")
    List<DeviceDto> getDevicesByOwner(@RequestParam String uid);
}
