package dev.jason.project.spring.vc_server.core.model;

import java.util.Map;

public record Message(String from, String to, String text, String timestamp) {

    public Map<String, String> asMap() {
        return Map.of(
            "received_from", from,
            "to", to,
            "text", text,
            "timestamp", timestamp
        );
    }
}
