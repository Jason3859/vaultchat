package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Device.OS;
import dev.jason.project.spring.vc_server.core.model.Device.Type;
import dev.jason.project.spring.vc_server.core.model.User.Status;

public record RegisterUserDto(
    String uid,
    String displayName,
    String profilePictureUrl,
    Device device
) {
	public record Device(
		String name,
		Type type,
		OS os,
		String version,
		String token
	) {}
	
	public UserDto asUserDto() {
		return new UserDto(uid, displayName, profilePictureUrl, Status.Online);
	}
	
	public DeviceDto asDeviceDto() {
		return new DeviceDto(uid, device.name(), device.type(), device.os(), device.version(), device.token());
	}
}
