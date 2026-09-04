package dev.jason.app.compose.vaultchat.core.model.message

import java.time.LocalDateTime

data class Message(
    val id: String? = null, // only null while sending to backend, not while receiving from backend.
    val from: String,
    val fromDisplayName: String?,
    val to: String,
    val text: String,
    val timestamp: LocalDateTime
) {
    constructor(
        id: String? = null,
        from: String,
        to: String,
        text: String,
        timestamp: LocalDateTime
    ) : this(id, from, null, to, text, timestamp)
}