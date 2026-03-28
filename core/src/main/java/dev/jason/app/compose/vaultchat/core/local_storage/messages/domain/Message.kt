package dev.jason.app.compose.vaultchat.core.local_storage.messages.domain

import java.time.LocalDateTime

data class Message(
    val from: String,
    val text: String,
    val at: LocalDateTime
)