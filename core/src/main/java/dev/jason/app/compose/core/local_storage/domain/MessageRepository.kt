package dev.jason.app.compose.core.local_storage.domain

interface MessageRepository {

    suspend fun addMessage(message: Message)
}