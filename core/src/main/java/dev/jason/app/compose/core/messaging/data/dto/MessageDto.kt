package dev.jason.app.compose.core.messaging.data.dto

import dev.jason.app.compose.core.messaging.domain.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val from: String,
    val to: String,
    val text: String
)

fun Message.toDto(): MessageDto = MessageDto(from, to, text)