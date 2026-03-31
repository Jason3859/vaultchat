package dev.jason.app.compose.vaultchat.core.local_storage.messages.domain

import dev.jason.app.compose.vaultchat.core.domain.Message

interface MessageRepository {

    suspend fun addMessage(message: Message)
}