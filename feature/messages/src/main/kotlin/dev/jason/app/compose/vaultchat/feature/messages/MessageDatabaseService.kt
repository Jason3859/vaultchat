package dev.jason.app.compose.vaultchat.feature.messages

import android.util.Log
import androidx.paging.PagingData
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.model.message.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MessageDatabaseService(
    private val repository: MessageDatabaseRepository
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getMessagesPaginated(currentUserUid: String, otherUserUid: String, coroutineScope: CoroutineScope): Flow<PagingData<Message>> =
        repository.getMessagesPaginated(currentUserUid, otherUserUid, coroutineScope)

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
                    Log.d("MessageDatabaseService", "init: received add message event")
                    addMessage(event.message)
                }

                if (event is AppEvent.DeleteAllMessages) {
                    deleteAllMessages()
                }
            }
        }
    }
}