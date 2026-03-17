package dev.jason.project.spring.vc_server.domain;

import dev.jason.project.spring.vc_server.service.VCUtilityService;

import java.util.Objects;

public record Message(String from, String to, String text, String timestamp) {

    public com.google.firebase.messaging.Message toMessage(String toFcmToken) {

        try {
            String displayName = Objects.requireNonNull(VCUtilityService.getUserFromUid(from)).displayName();

            return com.google.firebase.messaging.Message.builder()
                    .setToken(toFcmToken)
                    .putData("title", String.format("New message from %s", displayName))
                    .putData("body", text)
                    .putData("uid", from)
                    .putData("timestamp", timestamp)
                    .build();
        } catch (NullPointerException ignored) {
            return null;
        }
    }
}
