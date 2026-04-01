package dev.jason.app.compose.vaultchat.core.messaging.ui.screen.messaging

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.local_storage.messages.domain.MessageRepository
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.core.messaging.domain.remote.RemoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MessagingViewModel(
    private val otherUser: User,
    private val api: RemoteApi,
    private val messageRepository: MessageRepository
) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    data class UiState(
        val messageText: String = "",
        val sendButtonEnabled: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    private val _pendingMessages = mutableStateListOf<Message>()
    val pendingMessages: List<Message> = _pendingMessages

    private val _failedMessages = mutableStateListOf<Message>()
    val failedMessages: List<Message> = _failedMessages

    private val currentUserUid by lazy { Firebase.auth.currentUser!!.uid }

    fun updateState(state: UiState) {
        _uiState.update {
            state.copy(
                sendButtonEnabled = state.messageText.isNotBlank()
            )
        }
    }

    private fun updateState(state: (UiState) -> UiState) {
        updateState(state(_uiState.value))
    }


    fun sendMessage() {
        val state = _uiState.value.copy()
        if (!state.sendButtonEnabled) return

        val message = Message(
            from = currentUserUid,
            to = otherUser.uid,
            text = state.messageText,
            timestamp = LocalDateTime.now()
        )

        updateState {
            it.copy(messageText = "")
        }

        _messages.add(message)
        _pendingMessages.add(message)

        viewModelScope.launch {
            api.sendMessage(message)
                .onSuccess {
                    _pendingMessages.remove(message)
                    messageRepository.addMessage(message)
                }
                .onError {
                    Log.e(
                        "MessagingViewModel",
                        "sendMessage: error sending message. response from server: $it"
                    )
                    _pendingMessages.remove(message)
                    _failedMessages.add(message)
                }
        }
    }

    init {
        viewModelScope.launch {
            messageRepository.getMessages(currentUserUid, otherUser.uid).collect { messages ->
                if (_messages.isEmpty()) {
                    _messages.addAll(messages) // initial state
                } else {
                    val msg = messages.last()

                    if (msg.from != currentUserUid) {
                        _messages.add(msg) // new message from other user
                    }
                }
            }
        }
    }
}