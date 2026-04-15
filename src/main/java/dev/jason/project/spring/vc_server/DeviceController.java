package dev.jason.project.spring.vc_server;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.jason.project.spring.vc_server.domain.exception.VcException;
import dev.jason.project.spring.vc_server.dto.DeviceDto;
import dev.jason.project.spring.vc_server.dto.ResultDto;
import dev.jason.project.spring.vc_server.dto.ResultDto.Result;
import dev.jason.project.spring.vc_server.users.UserService;

@RestController
@RequestMapping("/devices")
public class DeviceController {

	@Autowired
	private UserService userService;

	@PostMapping("/add")
	public ResultDto addDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		try {
			userService.addDevice(uid, device.toDomainModel(LocalDateTime.now()));
			return new ResultDto(Result.Success);
		} catch (VcException e) {
			return ResultDto.fromVcException(e);
		}
	}

	@GetMapping("/my-devices")
	public ResultDto getMyDevices(@RequestParam String uid) {
		try {
			List<DeviceDto> devices = userService.getUserDevicesByUid(uid).stream()
					.map(DeviceDto::fromDomain).toList();
			return new ResultDto(Result.Success, devices);
		} catch (VcException e) {
			return ResultDto.fromVcException(e);
		}
	}

	@DeleteMapping("/delete")
	public ResultDto deleteDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		try {
			userService.deleteDevice(uid, device.toDomainModel(null));
			return new ResultDto(Result.Success);
		} catch (VcException e) {
			return ResultDto.fromVcException(e);
		}
	}
}
