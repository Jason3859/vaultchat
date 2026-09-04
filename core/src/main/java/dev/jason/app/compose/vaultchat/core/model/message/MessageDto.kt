package dev.jason.app.compose.vaultchat.core.model.message

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDto(
    val id: String?,
    val from: String,
    val fromDisplayName: String? = null,
    val to: String,
    val text: String,
    val timestamp: String
)

fun Message.toDto(): MessageDto = MessageDto(id, from, to = to, text = text, timestamp = timestamp.toString())
fun MessageDto.toMessage() = Message(id, from, to, text, LocalDateTime.parse(timestamp))