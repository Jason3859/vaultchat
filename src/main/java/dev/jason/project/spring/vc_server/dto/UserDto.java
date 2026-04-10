package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;

public record UserDto(String uid, String displayName, String profilePictureUrl, UserStatus status) {
	
	public static UserDto fromDomainUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status());
	}
}