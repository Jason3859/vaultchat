package dev.jason.app.compose.vaultchat.core.messaging.domain.remote

import dev.jason.app.compose.vaultchat.core.messaging.domain.model.ApiResult
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.Message
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.UserToken

interface RemoteApi {
    suspend fun sendMessage(body: Message)
    suspend fun updateFcmToken(body: UserToken)
    suspend fun searchUsers(name: String, from: String): ApiResult<List<User>>
    suspend fun getConnections(uid: String): ApiResult<List<User>>
}