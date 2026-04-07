package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult
import dev.jason.app.compose.vaultchat.messaging.domain.model.RegisterUser
import dev.jason.app.compose.vaultchat.messaging.domain.model.User

interface RemoteApiRepository {
    suspend fun sendMessage(body: Message): ApiResult<Void> // no data is returned
    suspend fun searchUsers(name: String, from: String): ApiResult<List<User>>
    suspend fun getConnections(uid: String): ApiResult<List<User>>
    suspend fun registerUser(body: RegisterUser)
}