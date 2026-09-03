package dev.jason.app.compose.vaultchat.core.model.message

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageDto(
    val from: String,
    val to: String,
    val text: String,
    val timestamp: String
)

fun Message.toDto(): MessageDto = MessageDto(from, to, text, timestamp.toString())
fun MessageDto.toMessage() = Message(from, to, text, LocalDateTime.parse(timestamp))