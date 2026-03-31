package dev.jason.app.compose.vaultchat.core.messaging.data.dto

import dev.jason.app.compose.vaultchat.core.domain.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val from: String,
    val to: String,
    val text: String,
    val timestamp: String
)

fun Message.toDto(): MessageDto = MessageDto(from, to, text, timestamp.toString())