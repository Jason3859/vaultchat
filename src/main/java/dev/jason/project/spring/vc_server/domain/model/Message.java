package dev.jason.project.spring.vc_server.domain.model;

import java.util.Map;

public record Message(String from, String to, String text, String timestamp) {

    public Map<String, String> asMap() {
        return Map.of(
            "received_from", from,
            "to", to,
            "title", "New Message!",
            "text", text,
            "timestamp", timestamp
        );
    }
}
