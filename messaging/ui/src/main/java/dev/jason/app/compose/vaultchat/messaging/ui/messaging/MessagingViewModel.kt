package dev.jason.app.compose.vaultchat.messaging.ui.messaging

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MessagingViewModel(
    private val otherUserUid: String,
    private val api: RemoteApiRepository,
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    data class UiState(
        val messageText: String = "",
        val sendButtonEnabled: Boolean = false,
    )

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser = _otherUser.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    private val _pendingMessages = mutableStateListOf<Message>()
    val pendingMessages: List<Message> = _pendingMessages

    private val _failedMessages = mutableStateListOf<Message>()
    val failedMessages: List<Message> = _failedMessages

    val currentUserUid = MessagingState.currentUser.value?.uid ?: throw IllegalStateException("User is null")

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
            to = otherUserUid,
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
                .let { statusCode ->
                    if (statusCode in 200..299) { // success
                        _pendingMessages.remove(message)
                        localStorageRepository.addMessage(message)
                    } else { // not success
                        Log.e(
                            "MessagingViewModel",
                            "sendMessage: error status code from server: $statusCode"
                        )

                        _pendingMessages.remove(message)
                        _failedMessages.add(message)
                    }
                }
        }
    }

    init {
        viewModelScope.launch {
            localStorageRepository.getMessages(currentUserUid, otherUserUid).collect { messages ->
                if (!_messages.isEmpty()) {
                    try {
                        val msg = messages.last()

                        if (msg.from != currentUserUid) {
                            _messages.add(msg) // new message from other user
                        }
                    } catch (_: NoSuchElementException) { // only thrown when user deletes chat history.
                        _messages.clear()
                    }
                } else {
                    _messages.addAll(messages) // initial state
                }
            }
        }

        viewModelScope.launch {
            localStorageRepository.getConnectionByUid(otherUserUid).collect { user ->
                _otherUser.update { user }
            }
        }
    }
}