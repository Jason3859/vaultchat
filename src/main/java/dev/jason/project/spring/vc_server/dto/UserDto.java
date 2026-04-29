package dev.jason.project.spring.vc_server.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.jason.project.spring.vc_server.model.User;

public record UserDto(String uid, String displayName, String profilePictureUrl, User.Status status, DeviceDto device) {
	
	public UserDto(String uid, String displayName, String profilePictureUrl, User.Status status) {
		this(uid, displayName, profilePictureUrl, status, null);
	}
	
	// Only when registering new user
	public UserDto(String uid, String displayName, String profilePictureUrl, DeviceDto device) {
		this(uid, displayName, profilePictureUrl, null, device);
	}
	
	public static UserDto fromDomainUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status());
	}
	
	public User asNewUser() {
		return new User(
            uid,
            displayName,
            profilePictureUrl,
            device == null ? new ArrayList<>(List.of()) : new ArrayList<>(List.of(device.toDomainModel(LocalDateTime.now()))),
            new ArrayList<>(List.of()),
            new ArrayList<>(List.of()),
            User.Status.Online,
            System.currentTimeMillis()
        );
	}
}