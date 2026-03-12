package dev.jason.app.compose.core.local_storage.domain

import java.time.LocalDateTime

data class Message(
    val from: String,
    val text: String,
    val at: LocalDateTime
)