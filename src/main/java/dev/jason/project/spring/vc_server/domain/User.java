package dev.jason.project.spring.vc_server.domain;

import java.util.List;

public record User(
    String uid,
    String displayName,
    String profilePictureUrl,
    List<Device> devices,
    List<String> connections,
    List<String> blocklist,
    UserStatus status,
    long lastHeartbeat
) {
}
