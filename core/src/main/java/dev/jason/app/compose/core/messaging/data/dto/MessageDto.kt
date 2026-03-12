package dev.jason.app.compose.core.messaging.data.dto

import dev.jason.app.compose.core.messaging.domain.model.Message
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