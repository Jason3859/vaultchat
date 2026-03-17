package dev.jason.project.spring.vc_server.domain;

import dev.jason.project.spring.vc_server.users.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class Message {

    private final String from;
    private final String to;
    private final String text;
    private final String timestamp;

    @Autowired
    private UserDbService service;

    public Message(String from, String to, String text, String timestamp) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public com.google.firebase.messaging.Message toMessage(String toFcmToken) {

        try {
            String displayName = Objects.requireNonNull(service.getUserByUid(from)).displayName();

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
