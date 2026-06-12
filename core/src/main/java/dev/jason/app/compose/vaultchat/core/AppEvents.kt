package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {

    private val _event = MutableSharedFlow<AppEvent>(replay = 0)
    val event = _event.asSharedFlow()

    fun sendEvent(event: AppEvent) {
        _event.tryEmit(event)
    }
}

sealed interface AppEvent {

    data class UpdateConnections(val connections: List<User>) : AppEvent
    data class UpdateConnectionStatus(val uid: String, val status: User.Status) : AppEvent

    data class AddMessage(val message: Message) : AppEvent
    data object DeleteAllMessages : AppEvent
}