package dev.jason.project.spring.vc_server.domain;

public record Message(String from, String to, String text, String timestamp) {

    public com.google.firebase.messaging.Message toMessage(String toFcmToken) {
        try {
            return com.google.firebase.messaging.Message.builder()
                .setToken(toFcmToken)
                .putData("title", "New message!")
                .putData("body", text)
                .putData("uid", from)
                .putData("timestamp", timestamp)
                .build();
        } catch (NullPointerException e) {
            Logger.write(e);
            return null;
        }
    }
}
