package dev.jason.app.compose.core.messaging.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.core.messaging.domain.Message
import dev.jason.app.compose.core.messaging.domain.RemoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AppViewModel(private val api: RemoteApi) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.UpdateRemoteToken -> {
                _uiState.update {
                    it.copy(remoteToken = action.newToken)
                }
            }

            is Action.SubmitRemoteToken -> {
                _uiState.update {
                    it.copy(
                        isEnteringToken = false
                    )
                }
            }

            is Action.UpdateMessage -> {
                _uiState.update {
                    it.copy(
                        messageText = action.message
                    )
                }
            }

            is Action.SendMessage -> sendMessage()
        }
    }

    private fun sendMessage() {
        viewModelScope.launch {
            val messageDto = Message(
                from = "name",
                to = _uiState.value.remoteToken,
                notification = Message.Notification(
                    title = "New message",
                    body = _uiState.value.messageText
                )
            )

            try {
                api.send(messageDto)
                _uiState.update { it.copy(messageText = "") }
            } catch (e: HttpException) {
                Log.e("AppViewModel", "sendMessage: exception", e)
            } catch (e: IOException) {
                Log.e("AppViewModel", "sendMessage: exception", e)
            }
        }
    }

    interface Action {
        data class UpdateRemoteToken(val newToken: String) : Action
        data class UpdateMessage(val message: String) : Action

        data object SubmitRemoteToken : Action
        data object SendMessage : Action
    }
}