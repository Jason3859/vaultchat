package dev.jason.app.compose.core.messaging.ui.screen.messaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.core.messaging.domain.model.Message
import dev.jason.app.compose.core.messaging.domain.remote.RemoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MessagingViewModel(private val api: RemoteApi) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    data class UiState(
        val messageText: String = "",
        val currentUserUid: String = "",
        val otherUserUid: String = "",
        val otherUserDisplayName: String = "",
        val sendButtonEnabled: Boolean = false,
        val messages: List<Message> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val currentUserUid by lazy { Firebase.auth.currentUser!!.uid }

    fun updateState(state: UiState) {
        _uiState.update {
            state.copy(
                currentUserUid = currentUserUid,
                sendButtonEnabled = state.messageText.isNotBlank()
            )
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            api.sendMessage(
                body = Message(currentUserUid, _uiState.value.otherUserUid, _uiState.value.messageText)
            )
        }
    }
}