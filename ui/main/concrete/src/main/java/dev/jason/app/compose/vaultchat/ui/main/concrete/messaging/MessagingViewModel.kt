package dev.jason.app.compose.vaultchat.ui.main.concrete.messaging

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.feature.connections.ConnectionsService
import dev.jason.app.compose.vaultchat.feature.messages.MessageDatabaseService
import dev.jason.app.compose.vaultchat.feature.messaging.MessagingApiService
import dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging.MessagingUiAction
import dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging.MessagingUiState
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.MessageUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.toUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MessagingViewModel(
    private val otherUserUid: String,
    private val connectionsService: ConnectionsService,
    private val messageDatabaseService: MessageDatabaseService,
    private val messagingApiService: MessagingApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagingUiState())
    val uiState = _uiState.asStateFlow()

    private val currentUser by lazy { AppState.currentUser.value!! }

    private val _otherUser = MutableStateFlow<UserUi?>(null)
    val otherUser = _otherUser.asStateFlow()

    private val _messages = MutableStateFlow<PagingData<MessageUi>>(PagingData.empty())
    val messages = _messages.asStateFlow()

    private val _pendingMessages = mutableStateListOf<MessageUi>()
    val pendingMessages: List<MessageUi> = _pendingMessages

    private val _failedMessages = mutableStateListOf<MessageUi>()
    val failedMessages: List<MessageUi> = _failedMessages

    fun onAction(action: MessagingUiAction) {
        when (action) {
            is MessagingUiAction.UpdateState -> updateState(action.state)
            is MessagingUiAction.SendMessage -> sendMessage()
        }
    }

    private fun updateState(state: (MessagingUiState) -> MessagingUiState) {
        _uiState.update {
            state(it).copy(
                sendButtonEnabled = it.messageText.isNotBlank()
            )
        }
    }

    private fun sendMessage() {
        if (!_uiState.value.sendButtonEnabled) return

        viewModelScope.launch {
            val message = Message(
                from = currentUser.uid,
                to = otherUserUid,
                text = _uiState.value.messageText,
                timestamp = LocalDateTime.now()
            )
            val messageUi = message.toUi()
            _pendingMessages.add(messageUi)

            messagingApiService.sendMessage(message).let { statusCode ->
                _pendingMessages.remove(messageUi)

                when (statusCode) {
                    in 200..299 -> messageDatabaseService.addMessage(message)
                    else -> _failedMessages.add(messageUi)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            connectionsService.getConnection(otherUserUid).let { user ->
                _otherUser.update { user.toUi() }
            }
            _uiState.update { it.copy(isLoading = false) }
        }

        viewModelScope.launch {
            messageDatabaseService.getMessagesPaginated(
                currentUserUid = currentUser.uid,
                otherUserUid = otherUserUid,
                coroutineScope = this
            ).map { pagingData ->
                pagingData.map { it.toUi() }
            }.cachedIn(this)
                .collect { data ->
                    _messages.value = data
                }
        }
    }
}
