package dev.jason.app.compose.vaultchat.feature_service.messages

import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessageDatabaseService(
    private val repository: MessageDatabaseRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getMessagesPaginated(from: String, to: String, coroutineScope: CoroutineScope): Flow<PagingData<Message>> =
        repository.getMessagesPaginated(from, to, coroutineScope)

    suspend fun addMessage(message: Message) =
        repository.addMessage(message)

    suspend fun deleteAllMessages() =
        repository.deleteAllMessages()

    suspend fun deleteMessageHistory(currentUserUid: String, otherUserUid: String) =
        repository.deleteMessageHistory(currentUserUid, otherUserUid)

    init {
        coroutineScope.launch {
            AppEvents.event.collect { event ->
                if (event is AppEvent.AddMessage) {
                    addMessage(event.message)
                }

                if (event is AppEvent.DeleteAllMessages) {
                    deleteAllMessages()
                }
            }
        }
    }
}