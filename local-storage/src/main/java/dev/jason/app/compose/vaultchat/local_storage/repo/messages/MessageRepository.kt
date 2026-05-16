package dev.jason.app.compose.vaultchat.local_storage.repo.messages

import dev.jason.app.compose.vaultchat.core.domain.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun addMessage(message: Message)
    suspend fun deleteChatHistory(currentUserUid: String, otherUserUid: String)
    fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<Message>>
}