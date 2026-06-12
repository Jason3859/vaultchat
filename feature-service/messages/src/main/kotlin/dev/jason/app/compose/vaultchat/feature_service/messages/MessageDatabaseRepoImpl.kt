package dev.jason.app.compose.vaultchat.feature_service.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.jason.app.compose.vaultchat.core.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageDatabaseRepoImpl(
    private val dao: MessageDao
) : MessageDatabaseRepository {

    override fun getMessagesPaginated(
        from: String,
        to: String,
        coroutineScope: CoroutineScope
    ): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getMessages(from, to) }
        ).flow
            .cachedIn(coroutineScope)
            .map { pagingData ->
                pagingData.map { entity ->
                    entity.toMessage()
                }
            }
    }

    override suspend fun deleteMessageHistory(
        currentUserUid: String,
        otherUserUid: String
    ) {
        dao.deleteMessageHistory(currentUserUid, otherUserUid)
    }

    override suspend fun addMessage(message: Message) {
        dao.addMessage(message.toEntity())
    }

    override suspend fun deleteAllMessages() {
        dao.deleteAllMessages()
    }
}