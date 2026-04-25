package dev.jason.project.spring.vc_server.domain.model;

import java.util.List;

public record User(
    String uid,
    String displayName,
    String profilePictureUrl,
    List<Device> devices,
    List<String> connections,
    List<String> blocklist,
    Status status,
    long lastHeartbeat
) {
    public enum Status {
        Online, Away, Offline
    }
}
