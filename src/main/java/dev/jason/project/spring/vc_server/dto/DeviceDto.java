package dev.jason.project.spring.vc_server.dto;

import dev.jason.project.spring.vc_server.domain.Device;

import java.time.LocalDateTime;

public record DeviceDto(String name, Device.Type type, Device.OS os, String version, String fcmToken) {

    public Device toDomainModel(LocalDateTime lastTimeUsed) {
        return new Device(name, type, os, version, fcmToken, lastTimeUsed);
    }
    
    public static DeviceDto fromDomain(Device device) {
    	return new DeviceDto(device.name(), device.type(), device.os(), device.version(), device.fcmToken());
    }
}
