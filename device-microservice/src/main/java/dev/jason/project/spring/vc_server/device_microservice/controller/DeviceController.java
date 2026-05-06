package dev.jason.project.spring.vc_server.device_microservice.controller;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.device_microservice.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
	
	@Autowired
	private DeviceService deviceService;

	@PostMapping("/add")
	public ResponseEntity<?> addDevice(@RequestBody DeviceDto device) {
		deviceService.addDevice(device.toDevice(LocalDateTime.now()));
		return new ResponseEntity<>(device, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteDevice(@RequestBody DeviceDto device) {
		deviceService.deleteDevice(device.toDevice(/*not required here*/ null));
		return new ResponseEntity<>(device, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/my-devices")
	public ResponseEntity<List<DeviceDto>> getDevices(@RequestParam String uid) {
		List<DeviceDto> devices = deviceService.getDevicesByOwner(uid)
			.stream()
			.map(DeviceDto::asDto)
			.toList();
		
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}

	@GetMapping("/get-devices-by-owner")
	public List<DeviceDto> getDevicesByOwner(@RequestParam String uid) {
        return deviceService.getDevicesByOwner(uid)
            .stream()
            .map(DeviceDto::asDto)
            .toList();
	}
}
