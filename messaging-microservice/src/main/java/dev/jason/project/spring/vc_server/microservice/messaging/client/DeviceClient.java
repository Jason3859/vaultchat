package dev.jason.project.spring.vc_server.microservice.messaging.client;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "device-microservice", url = Endpoints.DEVICE_SERVICE_URL)
public interface DeviceClient {

    @GetMapping(Endpoints.DEVICE_GET_DEVICES_BY_OWNER)
    List<DeviceDto> getDevicesByOwner(@RequestParam String uid);
    
    @PostMapping(Endpoints.DEVICE_VERIFY)
    void verifyDevice(@RequestBody DeviceDto device);
}
