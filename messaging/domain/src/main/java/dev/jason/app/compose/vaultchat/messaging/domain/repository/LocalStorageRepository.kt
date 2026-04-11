package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {

    suspend fun addMessage(message: Message)
    fun getMessages(currentUserId: String, otherUserId: String): Flow<List<Message>>
}