package dev.jason.project.spring.vc_server.data.dto;

import dev.jason.project.spring.vc_server.domain.model.User;

public record UserDto(String uid, String displayName, String profilePictureUrl, User.Status status) {
	
	public static UserDto fromDomainUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status());
	}
}