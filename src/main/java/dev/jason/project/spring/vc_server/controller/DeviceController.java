package dev.jason.project.spring.vc_server.controller;

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

import dev.jason.project.spring.vc_server.data.dto.DeviceDto;
import dev.jason.project.spring.vc_server.data.dto.ResultDto;
import dev.jason.project.spring.vc_server.domain.exception.DeviceAlreadyExistsException;
import dev.jason.project.spring.vc_server.domain.exception.UserNotFoundException;
import dev.jason.project.spring.vc_server.domain.exception.VcException;
import dev.jason.project.spring.vc_server.domain.model.Result;
import dev.jason.project.spring.vc_server.domain.service.UserDeviceService;

@RestController
@RequestMapping("/user/devices")
public class DeviceController {

	@Autowired
	private UserDeviceService userDeviceService;

	@PostMapping("/add")
	public ResultDto addDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		try {
			userDeviceService.addDevice(uid, device.toDomainModel(LocalDateTime.now()));
			return new ResultDto(Result.Success);
		} catch (UserNotFoundException | DeviceAlreadyExistsException e) {
			return ResultDto.fromVcException(e);
		}
	}

	@GetMapping("/my-devices")
	public ResultDto getMyDevices(@RequestParam String uid) {
		try {
			List<DeviceDto> devices = userDeviceService.getUserDevicesByUid(uid).stream()
					.map(DeviceDto::fromDomain).toList();
			return new ResultDto(Result.Success, devices);
		} catch (VcException e) {
			return ResultDto.fromVcException(e);
		}
	}

	@DeleteMapping("/delete")
	public ResultDto deleteDevice(@RequestParam String uid, @RequestBody DeviceDto device) {
		try {
			userDeviceService.deleteDevice(uid, device.toDomainModel(null));
			return new ResultDto(Result.Success);
		} catch (VcException e) {
			return ResultDto.fromVcException(e);
		}
	}
	
    @PostMapping("/update-token")
    public ResultDto updateToken(@RequestParam String uid, @RequestParam String token, @RequestBody DeviceDto device) {
        try {
            userDeviceService.updateToken(uid, token, device.toDomainModel(LocalDateTime.now()));
            return new ResultDto(Result.Success);
        } catch (VcException e) {
            return ResultDto.fromVcException(e);
        }
    }
}
