package dev.jason.app.compose.vaultchat.feature.messages

import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.model.message.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MessageDatabaseRepository {

    fun getMessagesPaginated(currentUserUid: String, otherUserUid: String, coroutineScope: CoroutineScope): Flow<PagingData<Message>>

    suspend fun addMessage(message: Message)
    suspend fun deleteAllMessages()
    suspend fun deleteMessageHistory(currentUserUid: String, otherUserUid: String)
}