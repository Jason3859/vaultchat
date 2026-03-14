package dev.jason.app.compose.core.local_storage.messages.domain

interface MessageRepository {

    suspend fun addMessage(message: Message)
}