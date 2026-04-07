package dev.jason.app.compose.vaultchat.messaging.data.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.local_storage.domain.MessageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow

class LocalStorageRepoImpl(private val messageRepository: MessageRepository) : LocalStorageRepository {

    override suspend fun addMessage(message: Message) {
        messageRepository.addMessage(message)
    }

    override fun getMessages(currentUserId: String, otherUserId: String): Flow<List<Message>> {
        return messageRepository.getMessages(currentUserId, otherUserId)
    }
}