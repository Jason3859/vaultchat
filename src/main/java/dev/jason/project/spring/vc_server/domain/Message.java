package dev.jason.project.spring.vc_server.domain;

import dev.jason.project.spring.vc_server.service.VCUtilityService;

public record Message(String from, String to, String text, String timestamp) {

    public com.google.firebase.messaging.Message toMessage(String toFcmToken) {
        return com.google.firebase.messaging.Message.builder()
            .setToken(toFcmToken)
            .putData("title", String.format("New message from %s", VCUtilityService.getUserFromUid(from).displayName()))
            .putData("body", text)
            .putData("uid", from)
            .putData("timestamp", timestamp)
            .build();
    }
}
