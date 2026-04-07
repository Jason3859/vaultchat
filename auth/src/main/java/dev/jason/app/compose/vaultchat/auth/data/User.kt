package dev.jason.app.compose.vaultchat.auth.data

data class User(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val fcmToken: String
)
