package dev.jason.app.compose.vaultchat.messaging.domain.model

data class RegisterUser(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val token: String
)
