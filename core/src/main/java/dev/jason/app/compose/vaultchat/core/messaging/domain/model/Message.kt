package dev.jason.app.compose.vaultchat.core.messaging.domain.model

import java.time.LocalDateTime

data class Message(
    val from: String,
    val to: String,
    val text: String,
    val timestamp: LocalDateTime
)