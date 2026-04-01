package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.users.UserEntity;

public record AddUserDto(String uid, String displayName, String profilePictureUrl, String fcmToken) {

    public AddUserDto(String uid) {
        this(uid, uid, null, null);
    }

    public UserEntity toDbUser() {
        return new UserEntity(uid, displayName, profilePictureUrl, fcmToken, null, null);
    }
}
