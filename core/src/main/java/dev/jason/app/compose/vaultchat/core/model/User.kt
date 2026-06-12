package dev.jason.app.compose.vaultchat.core.model

data class User(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status
) {
    enum class Status {
        Online, Offline
    }
}