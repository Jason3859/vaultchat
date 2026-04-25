package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult

interface RemoteApiRepository {

    suspend fun fetchConnections(uid: String): ApiResult<List<User>>
    suspend fun fetchDevices(uid: String): ApiResult<List<Device>>
    suspend fun fetchBlocklist(uid: String): ApiResult<List<User>>

    suspend fun block(uid: String, uidToBlock: String): ApiResult<Void>
    suspend fun unblock(uid: String, uidToUnblock: String): ApiResult<Void>

    suspend fun updateStatus(uid: String, status: User.Status): ApiResult<Void>

    suspend fun updateToken(uid: String, token: String, device: Device)

    suspend fun searchUsers(name: String, from: String): ApiResult<List<User>>

    suspend fun sendMessage(body: Message): ApiResult<Void> // no data is returned
}