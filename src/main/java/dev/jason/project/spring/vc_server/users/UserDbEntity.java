package dev.jason.project.spring.vc_server.users;

import dev.jason.project.spring.vc_server.domain.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record UserDbEntity(@Id String uid, String displayName, String profilePictureUrl, String fcmToken, String[] connections) {

    public User toDomainUser() {
        return new User(uid, displayName, profilePictureUrl);
    }
}