package dev.jason.project.spring.vc_server.core.dto;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;

public record UserDto(String uid, String displayName, String profilePictureUrl, User.Status status, DeviceDto device) {
	
	public UserDto(String uid, String displayName, String profilePictureUrl, User.Status status) {
		this(uid, displayName, profilePictureUrl, status, null);
	}
	
	public static UserDto fromUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status());
	}
	
	public static UserDto fromUserAndDevice(User user, Device device) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status(), DeviceDto.asDto(device));
	}
	
	public User asUser() {
		return new User(
            uid,
            displayName,
            profilePictureUrl,
            User.Status.Online,
            System.currentTimeMillis()
        );
	}
}