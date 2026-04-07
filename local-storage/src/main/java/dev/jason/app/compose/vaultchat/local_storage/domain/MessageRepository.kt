package dev.jason.app.compose.vaultchat.local_storage.domain

import dev.jason.app.compose.vaultchat.core.domain.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun addMessage(message: Message)
    fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<Message>>
}