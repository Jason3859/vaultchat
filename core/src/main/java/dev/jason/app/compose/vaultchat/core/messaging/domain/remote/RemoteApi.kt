package dev.jason.app.compose.vaultchat.core.messaging.domain.remote

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.ApiResult
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.RegisterUser
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User

interface RemoteApi {
    suspend fun sendMessage(body: Message): ApiResult<Void> // no data is returned
    suspend fun searchUsers(name: String, from: String): ApiResult<List<User>>
    suspend fun getConnections(uid: String): ApiResult<List<User>>
    suspend fun registerUser(body: RegisterUser)
}