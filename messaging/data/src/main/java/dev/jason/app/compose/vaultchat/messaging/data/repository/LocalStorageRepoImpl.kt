package dev.jason.app.compose.vaultchat.messaging.data.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.local_storage.repo.connections.ConnectionsRepository
import dev.jason.app.compose.vaultchat.local_storage.repo.messages.MessageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow

class LocalStorageRepoImpl(
    private val messageRepository: MessageRepository,
    private val connectionsRepository: ConnectionsRepository
) : LocalStorageRepository {

    override suspend fun addMessage(message: Message) {
        messageRepository.addMessage(message)
    }

    override suspend fun deleteMessageHistory(currentUserId: String, otherUserId: String) {
        messageRepository.deleteChatHistory(currentUserId, otherUserId)
    }

    override suspend fun deleteAllMessages() {
        messageRepository.deleteAllMessages()
    }

    override fun getMessages(currentUserId: String, otherUserId: String): Flow<List<Message>> {
        return messageRepository.getMessages(currentUserId, otherUserId)
    }

    override suspend fun updateStatus(uid: String, status: User.Status) {
        connectionsRepository.updateStatus(uid, status)
    }

    override suspend fun addAllConnections(users: List<User>) {
        connectionsRepository.addAllConnections(users)
    }

    override fun getConnections(): Flow<List<User>> {
        return connectionsRepository.getConnections()
    }

    override fun getConnectionByUid(uid: String): Flow<User> {
        return connectionsRepository.getConnectionById(uid)
    }
}