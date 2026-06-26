package dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging

interface MessagingUiAction {

    data class UpdateState(val state: (MessagingUiState) -> MessagingUiState) : MessagingUiAction

    data object SendMessage : MessagingUiAction
}