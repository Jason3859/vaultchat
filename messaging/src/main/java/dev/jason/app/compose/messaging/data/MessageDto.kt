package dev.jason.app.compose.messaging.data

import dev.jason.app.compose.messaging.domain.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val from: String,
    val to: String,
    val notification: NotificationDto
) {
    @Serializable
    data class NotificationDto(
        val title: String,
        val body: String
    )
}

fun Message.toDto(): MessageDto = MessageDto(from, to, notification.toDto())
fun Message.Notification.toDto() = MessageDto.NotificationDto(title, body)