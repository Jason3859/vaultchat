package dev.jason.app.compose.vaultchat.messaging.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResult<T>(val result: Result, val data: T? = null) {

    enum class Result {
        Success, InternalServerError, BlockedByUser,
        AlreadyBlocked, SelfBlock, UserAlreadyExists,
        SelfUnblock, MessageTextBlank, UserNotFound,
        NoBlockedUsers, NoUsersFound, UserNotBlocked, InternalError
    }

    inline fun onSuccess(onSuccessListener: () -> Unit): ApiResult<T> {
        if (result == Result.Success) {
            onSuccessListener()
        }

        return this
    }
    
    inline fun onError(onErrorListener: (Result) -> Unit): ApiResult<T> {
        if (result != Result.Success) {
            onErrorListener(result)
        }

        return this
    }
}
