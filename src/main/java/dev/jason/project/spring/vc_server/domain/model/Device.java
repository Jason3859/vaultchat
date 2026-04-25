package dev.jason.project.spring.vc_server.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Device {
    private final String name;
    private final Type type;
    private final OS os;
    private final String version;
    private String fcmToken;
    private final LocalDateTime lastUsed;

    public Device(String name, Type type, OS os, String version, String fcmToken, LocalDateTime lastUsed) {
        this.name = name;
        this.type = type;
        this.os = os;
        this.version = version;
        this.fcmToken = fcmToken;
        this.lastUsed = lastUsed;
    }

    public enum OS {
        Android // currently only android is supported. may add support to other oses in the future.
    }

    public enum Type {
        Mobile, Tablet // currently only mobile and tablet are supported. may add support to other devices in the future.
    }

    public String name() {
        return name;
    }
    
    public Type type() {
    	return type;
    }
    
    public OS os() {
    	return os;
    }
    
    public String version() {
    	return version;
    }

    public String token() {
        return fcmToken;
    }

    public void setFcmToken(String token) {
        this.fcmToken = token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, os, version, fcmToken, lastUsed);
    }

    @Override
    public String toString() {
        return "Device[" +
            "name=" + name + ", " +
            "type=" + type + ", " +
            "os=" + os + ", " +
            "version=" + version + ", " +
            "token=" + fcmToken + ", " +
            "lastUsed=" + lastUsed + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Device)) return false;
        return ((Device) other).fcmToken.equals(fcmToken); // returns true if fcm token is same as it will be different for each device
    }

}
