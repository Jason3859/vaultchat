package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.User;
import dev.jason.project.spring.vc_server.domain.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record RegisterUserDto(String uid, String displayName, String profilePictureUrl, DeviceDto device) {

    // only for testing
    public RegisterUserDto(String uid) {
        this(uid, uid, null, null);
    }

    public User toDomainUser() {
        return new User(uid,
            displayName,
            profilePictureUrl,
            device == null ? new ArrayList<>(List.of()) : new ArrayList<>(List.of(device.toDomainModel(LocalDateTime.now()))),
            new ArrayList<>(List.of()),
            new ArrayList<>(List.of()),
            UserStatus.Online,
            System.currentTimeMillis()
        );
    }

}
