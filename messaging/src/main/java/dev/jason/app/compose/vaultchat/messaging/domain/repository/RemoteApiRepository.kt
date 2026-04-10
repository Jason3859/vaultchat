package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult

interface RemoteApiRepository {
    suspend fun sendMessage(body: Message): ApiResult<Void> // no data is returned
    suspend fun searchUsers(name: String, from: String): ApiResult<List<User>>
    suspend fun getConnections(uid: String): ApiResult<List<User>>
    suspend fun updateStatus(uid: String, status: User.Status): ApiResult<Void>
    suspend fun heartbeat(uid: String)
}