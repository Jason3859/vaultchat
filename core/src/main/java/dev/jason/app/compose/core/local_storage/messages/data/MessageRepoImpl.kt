package dev.jason.app.compose.core.local_storage.messages.data

import dev.jason.app.compose.core.local_storage.messages.domain.Message
import dev.jason.app.compose.core.local_storage.messages.domain.MessageRepository

class MessageRepoImpl(private val dao: MessageDao) : MessageRepository {

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toDbEntity())
    }
}