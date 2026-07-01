package dev.jason.project.spring.vc_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.client.DeviceClient;
import dev.jason.project.spring.vc_server.client.SocialClient;
import dev.jason.project.spring.vc_server.client.UserClient;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import feign.FeignException;

@RestController
public class VcRestController {
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private DeviceClient deviceClient;
	
	@Autowired
	private SocialClient socialClient;
	
	@GetMapping
	public String home() {
		return "Hello, World!";
	}
	
	@PostMapping("register-user")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {
		registerUser(dto.asUserDto());
		addDevice(dto.asDeviceDto());
		registerSocialUser(dto.uid());
		
		return dto;
	}
	
	private void registerUser(UserDto dto) {
		try {
			userClient.registerUser(dto);
		} catch (FeignException.Conflict _) {
		}
	}
	
	private void addDevice(DeviceDto dto) {
		try {
			deviceClient.addDevice(dto);
		} catch (FeignException.Conflict e) {
			throw new DeviceAlreadyExistsException();
		}
	}
	
	private void registerSocialUser(String uid) {
		try {
			socialClient.registerUser(uid);
		} catch (FeignException.Conflict _) {
		}
	}
}
