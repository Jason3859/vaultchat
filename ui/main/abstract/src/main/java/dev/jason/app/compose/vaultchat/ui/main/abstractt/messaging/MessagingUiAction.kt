package dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging

import dev.jason.app.compose.vaultchat.core.model.Message

interface MessagingUiAction {

    data class UpdateState(val state: (MessagingUiState) -> MessagingUiState) : MessagingUiAction

    data class SendMessage(val message: Message) : MessagingUiAction {
        companion object : MessagingUiAction
    }
}