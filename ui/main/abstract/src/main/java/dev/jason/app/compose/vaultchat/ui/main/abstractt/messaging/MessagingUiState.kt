package dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging

data class MessagingUiState(
    val messageText: String = "",
    val sendButtonEnabled: Boolean = false,
    val isLoading: Boolean = false,
)