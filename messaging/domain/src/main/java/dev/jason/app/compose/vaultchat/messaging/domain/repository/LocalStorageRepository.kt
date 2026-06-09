package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {

    suspend fun addMessage(message: Message)
    suspend fun deleteMessageHistory(currentUserId: String, otherUserId: String)
    suspend fun deleteAllMessages()

    fun getMessages(currentUserId: String, otherUserId: String): Flow<List<Message>>
    suspend fun updateStatus(uid: String, status: User.Status)
    suspend fun addAllConnections(users: List<User>)
    fun getConnections(): Flow<List<User>>
    fun getConnectionByUid(uid: String): Flow<User>
}