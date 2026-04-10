package dev.jason.project.spring.vc_server.domain;

import java.time.LocalDateTime;

public record Device(String name, OS os, String version, String fcmToken, LocalDateTime lastUsed) {

    public enum OS {
        Android // currently only android is supported. may add support to other oses in the future.
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Device)) return false;
        return ((Device) other).fcmToken.equals(fcmToken);
    }
}
