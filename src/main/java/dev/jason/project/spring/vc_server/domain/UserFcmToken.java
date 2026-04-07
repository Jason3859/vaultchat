package dev.jason.project.spring.vc_server.domain;

import java.time.LocalDateTime;

public record UserFcmToken(String token, LocalDateTime lastUsed) {
}
