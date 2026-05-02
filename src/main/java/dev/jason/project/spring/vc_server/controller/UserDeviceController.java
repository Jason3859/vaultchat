package dev.jason.project.spring.vc_server.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.service.UserService;

@RestController
@RequestMapping("/device")
public class UserDeviceController {
	
	@Autowired
	private UserService userService;

	@PostMapping("/add")
	public ResponseEntity<?> addDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		userService.addDevice(uid, device.toDomainModel(LocalDateTime.now()));
		return new ResponseEntity<>(device, HttpStatus.CREATED);
	}
	
	@PatchMapping("/update-token")
	public ResponseEntity<?> updateDeviceToken(
	    @RequestParam String uid, 
	    @RequestParam String newToken, 
	    @RequestBody DeviceDto device
	) {
		userService.updateToken(uid, newToken, device.toDomainModel(null));
		return new ResponseEntity<>(device, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		userService.deleteDevice(uid, device.toDomainModel(/*not required here*/ null));
		return new ResponseEntity<>(device, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/my-devices")
	public ResponseEntity<List<DeviceDto>> getDevices(@RequestParam String uid) {
		List<DeviceDto> devices = userService.getUserByUid(uid)
			.devices()
			.stream()
			.map(DeviceDto::fromDomain)
			.toList();
		
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}
}
