package dev.jason.app.compose.core.messaging.domain

data class Message(
    val from: String,
    val to: String,
    val notification: Notification
) {
    data class Notification(
        val title: String,
        val body: String
    )
}