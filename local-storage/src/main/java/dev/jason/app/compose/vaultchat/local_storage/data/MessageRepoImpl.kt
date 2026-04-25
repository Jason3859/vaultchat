package dev.jason.app.compose.vaultchat.local_storage.data

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.local_storage.domain.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepoImpl(private val dao: MessageDao) : MessageRepository {

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toDbEntity())
    }

    override suspend fun deleteChatHistory(currentUserUid: String, otherUserUid: String) {
        dao.deleteMessageHistory(currentUserUid, otherUserUid)
    }

    override fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<Message>> {
        return dao.getMessages(currentUserUid, otherUserUid).map { it.map(MessageEntity::toDomain) }
    }
}