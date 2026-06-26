package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppRequests {

    private val _requests = MutableSharedFlow<AppRequest>(
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val requests = _requests.asSharedFlow()

    private val _responses = MutableSharedFlow<AppRequest.Response>(
        extraBufferCapacity = 128,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val responses = _responses.asSharedFlow()

    fun sendRequest(request: AppRequest) {
        _requests.tryEmit(request)
    }

    fun sendResponse(response: AppRequest.Response) {
        _responses.tryEmit(response)
    }
}

sealed interface AppRequest {

    data class GetConnectionRequest(val uid: String) : AppRequest

    sealed interface Response {
        data class GetConnectionResponse(val connection: User?) : Response
    }
}