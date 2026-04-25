package dev.jason.project.spring.vc_server.data.dto;

import dev.jason.project.spring.vc_server.domain.model.Device;

import java.time.LocalDateTime;

public record DeviceDto(String name, Device.Type type, Device.OS os, String version, String token) {

    public Device toDomainModel(LocalDateTime lastTimeUsed) {
        return new Device(name, type, os, version, token, lastTimeUsed);
    }
    
    public static DeviceDto fromDomain(Device device) {
    	return new DeviceDto(device.name(), device.type(), device.os(), device.version(), device.token());
    }
}
