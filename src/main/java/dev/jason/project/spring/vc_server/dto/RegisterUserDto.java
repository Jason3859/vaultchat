package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserFcmToken;

import java.time.LocalDateTime;
import java.util.List;

public record RegisterUserDto(String uid, String displayName, String profilePictureUrl, String fcmToken) {

	// only for testing
	public RegisterUserDto(String uid) {
		this(uid, uid, null, null);
	}

	public User toDomainUser() {
		UserFcmToken token = new UserFcmToken(fcmToken, LocalDateTime.now());
		return new User(uid, displayName, profilePictureUrl, List.of(token), null, null);
	}

}
