package dev.jason.app.compose.vaultchat.web_socket.domain.model

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val roomId: String,
    val sender: User,
    val text: String,
    val timestamp: LocalDateTime,
)
