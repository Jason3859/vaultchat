package dev.jason.project.spring.vc_server.domain;

import com.google.firebase.messaging.AndroidConfig;

import java.time.Duration;

public record Message(String from, String to, String text, String timestamp) {

    public com.google.firebase.messaging.Message toFirebaseMessage(String toFcmToken) {

        AndroidConfig androidConfig = AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.HIGH)
            .setTtl(Duration.ofMinutes(1).toMillis())
            .build();

        return com.google.firebase.messaging.Message.builder()
            .setToken(toFcmToken)
            .putData("is_message", String.valueOf(true))
            .putData("received_from", from)
            .putData("to", to)
            .putData("title", "New message!")
            .putData("text", text)
            .putData("timestamp", timestamp)
            .setAndroidConfig(androidConfig)
            .build();
    }
}
