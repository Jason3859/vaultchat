package dev.jason.app.compose.vaultchat.local_storage.repo.messages

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.local_storage.messages.MessageDao
import dev.jason.app.compose.vaultchat.local_storage.messages.MessageEntity
import dev.jason.app.compose.vaultchat.local_storage.messages.toDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepoImpl(private val dao: MessageDao) : MessageRepository {

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toDbEntity())
    }

    override suspend fun deleteChatHistory(currentUserUid: String, otherUserUid: String) {
        dao.deleteMessageHistory(currentUserUid, otherUserUid)
    }

    override suspend fun deleteAllMessages() {
        dao.deleteAllMessages()
    }

    override fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<Message>> {
        return dao.getMessages(currentUserUid, otherUserUid).map { it.map(MessageEntity::toDomain) }
    }
}