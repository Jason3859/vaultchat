package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.Device;

import java.time.LocalDateTime;

public record DeviceDto(String name, Device.OS os, String version, String fcmToken) {

    public Device toDomainModel(LocalDateTime lastTimeUsed) {
        return new Device(name, os, version, fcmToken, lastTimeUsed);
    }
}
