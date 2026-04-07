package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;

public record UserDto(String uid, String displayName, String profilePictureUrl) {
	
	public static UserDto fromDomainUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl());
	}
}