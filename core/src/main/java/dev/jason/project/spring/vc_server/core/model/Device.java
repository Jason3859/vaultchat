package dev.jason.project.spring.vc_server.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Getter
public final class Device {
    private final String ownerUid;
    private final String name;
    private final Type type;
    private final OS os;
    private final String version;
    @Setter
    private String token;
    private final LocalDateTime lastUsed;

    public enum OS {
        Android // currently only android is supported. may add support to other oses in the future.
    }

    public enum Type {
        Mobile, Tablet // currently only mobile and tablet are supported. may add support to other devices in the future.
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, os, version, token, lastUsed);
    }

    @Override
    public String toString() {
        return "Device[" +
            "name=" + name + ", " +
            "type=" + type + ", " +
            "os=" + os + ", " +
            "version=" + version + ", " +
            "lastUsed=" + lastUsed + ']';
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Device)) return false;
        return Objects.equals(((Device) other).name, name) &&
            Objects.equals(((Device) other).token, token); // returns true if fcm token and name are same
    }

}
