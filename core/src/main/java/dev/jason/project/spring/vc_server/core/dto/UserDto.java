package dev.jason.project.spring.vc_server.core.dto;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.User.Status;

public record UserDto(String uid, String displayName, String profilePictureUrl, Status status) {
	
	public static UserDto fromUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status());
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