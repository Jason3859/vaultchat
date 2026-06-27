package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.user.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {

    private val _events = MutableSharedFlow<AppEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val _requests = MutableSharedFlow<AppEvent.Request>(
        replay = 0,
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val requests = _requests.asSharedFlow()

    private val _responses = MutableSharedFlow<AppEvent.Response>(
        replay = 0,
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val responses = _responses.asSharedFlow()

    fun sendEvent(event: AppEvent) {
        _events.tryEmit(event)
    }

    fun sendRequest(request: AppEvent.Request) {
        _requests.tryEmit(request)
    }

    fun sendResponse(response: AppEvent.Response) {
        _responses.tryEmit(response)
    }
}

sealed interface AppEvent {

    data class UpdateConnections(val connections: List<User>) : AppEvent
    data class UpdateConnectionStatus(val uid: String, val status: User.Status) : AppEvent

    data class AddMessage(val message: Message) : AppEvent
    data object DeleteAllMessages : AppEvent

    data object ReFetchConnections : AppEvent

    sealed interface NavEvent : AppEvent {

        data class NavigateToMessagingScreen(val uid: String) : NavEvent
        data object NavigateToHomeScreen : NavEvent
    }

    sealed interface Request : AppEvent {
        data class GetConnectionRequest(val uid: String) : Request
    }

    sealed interface Response : AppEvent {
        data class GetConnectionResponse(val user: User?) : Response
    }
}