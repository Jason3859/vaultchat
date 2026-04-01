package dev.jason.app.compose.vaultchat.core.local_storage.messages.data

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.local_storage.messages.domain.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepoImpl(private val dao: MessageDao) : MessageRepository {

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toDbEntity())
    }

    override fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<Message>> {
        return dao.getMessages(currentUserUid, otherUserUid).map { it.map(MessageEntity::toDomain) }
    }
}