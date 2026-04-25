package dev.jason.project.spring.vc_server.data.dto;

import dev.jason.project.spring.vc_server.domain.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record RegisterUserDto(String uid, String displayName, String profilePictureUrl, DeviceDto device) {

    public User toDomainUser() {
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
