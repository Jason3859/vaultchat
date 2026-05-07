package dev.jason.project.spring.vc_server.core.dto;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.time.LocalDateTime;

public record DeviceDto(String ownerId, String name, Device.Type type, Device.OS os, String version, String token) {
	
	public DeviceDto(String name, Device.Type type, Device.OS os, String version, String token) {
		this("", name, type, os, version, token);
	}

    public Device toDevice(LocalDateTime lastTimeUsed) {
        return new Device(ownerId, name, type, os, version, token, lastTimeUsed);
    }
    
    public Device toDevice(String ownerId, LocalDateTime lastTimeUsed) {
    	return new Device(ownerId, name, type, os, version, token, lastTimeUsed);
    }
    
    public static DeviceDto asDto(Device device) {
    	return new DeviceDto(device.getOwnerId(), device.getName(), device.getType(), device.getOs(), device.getVersion(), device.getToken());
    }
}
