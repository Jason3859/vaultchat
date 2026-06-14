package dev.jason.app.compose.vaultchat.ui.abstractt.messaging.messaging

data class MessagingUiState(
    val messageText: String = "",
    val sendButtonEnabled: Boolean = false,
)