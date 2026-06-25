package dev.jason.app.compose.vaultchat.ui.main.concrete.messaging

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
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
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.toUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class MessagingViewModel(
    private val otherUserUid: String,
    private val otherUserFromConstructor: UserUi,
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
            is MessagingUiAction.SendMessage -> sendMessage(action.message)
            is MessagingUiAction.SendMessage.Companion -> sendMessage()
        }
    }

    private fun updateState(state: (MessagingUiState) -> MessagingUiState) {
        _uiState.update {
            state(it).copy(
                sendButtonEnabled = it.messageText.isNotBlank()
            )
        }
    }

    private fun sendMessage(msg: Message? = null) {
        val uiState = _uiState.value.copy()

        updateState { currentState ->
            currentState.copy(
                messageText = ""
            )
        }

        viewModelScope.launch {
            val message = msg ?: Message(
                from = currentUser.uid,
                to = otherUserUid,
                text = uiState.messageText,
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

            // value of `msg` will be null only
            // if the other user is not connected to current user
            // if the user sent a message means that the 2 users are connected
            // so, refetch connections for this
            if (msg != null) {
                AppEvents.sendEvent(AppEvent.ReFetchConnections)
            }
        }
    }

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (otherUserFromConstructor != UserUi.emptyUser()) {
                _otherUser.update { otherUserFromConstructor }
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val user = connectionsService.getConnection(otherUserUid)
            _otherUser.update { user?.toUi() }
            AppState.updateOtherUser(_otherUser.value?.toUser())
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
