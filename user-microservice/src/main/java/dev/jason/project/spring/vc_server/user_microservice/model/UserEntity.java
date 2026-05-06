package dev.jason.project.spring.vc_server.user_microservice.model;

import dev.jason.project.spring.vc_server.core.model.User;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Document(collection = "user")
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {

	@Id
	private final String uid;
	private String displayName;
	private String profilePictureUrl;
	private User.Status status;
	private long lastHeartBeat;
	
	public User asUser() {
		return new User(uid, displayName, profilePictureUrl, status, lastHeartBeat);
	}
	
	public static UserEntity asEntity(User user) {
		return new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.status(), user.lastHeartbeat());
	}
}
