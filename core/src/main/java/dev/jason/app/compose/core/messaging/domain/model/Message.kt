package dev.jason.app.compose.core.messaging.domain.model

data class Message(
    val from: String,
    val to: String,
    val text: String
)