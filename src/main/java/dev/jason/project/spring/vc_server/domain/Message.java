package dev.jason.project.spring.vc_server.domain;

import com.google.firebase.messaging.Notification;
import dev.jason.project.spring.vc_server.service.VCUtilityService;

public record Message(String from, String to, String text) {

    public com.google.firebase.messaging.Message toMessage(String toFcmToken) {
        Notification firebaseNotification = Notification.builder()
            .setTitle(String.format("New message from %s", VCUtilityService.getUserFromUid(from).displayName()))
            .setBody(text)
            .build();

        return com.google.firebase.messaging.Message.builder()
            .setNotification(firebaseNotification)
            .setToken(toFcmToken)
            .build();
    }
}
