package dev.jason.app.compose.vaultchat.core.local_storage.messages.domain

interface MessageRepository {

    suspend fun addMessage(message: Message)
}