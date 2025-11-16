package dev.jason.project.ktor.messenger.domain.model

import java.time.LocalDateTime

data class Message(
    val id: Long,
    val roomId: String,
    val senderUid: String,
    val message: String,
    val timestamp: LocalDateTime
)