package dev.jason.app.compose.vaultchat.web_socket.ui.model

data class UiMessage(
    val id: String,
    val roomId: String,
    val user: UiUser,
    val text: String,
    val timestamp: String
)