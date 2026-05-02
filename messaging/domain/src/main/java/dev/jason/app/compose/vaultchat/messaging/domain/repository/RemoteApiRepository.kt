package dev.jason.app.compose.vaultchat.messaging.domain.repository

import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User

interface RemoteApiRepository {

    suspend fun fetchBlocklist(uid: String): List<User>
    suspend fun block(uid: String, uidToBlock: String)
    suspend fun unblock(uid: String, uidToUnblock: String)

    suspend fun fetchConnections(uid: String): List<User>

    suspend fun searchUsers(name: String, from: String): List<User>

    suspend fun fetchDevices(uid: String): List<Device>
    suspend fun updateStatus(uid: String, status: User.Status)
    suspend fun updateToken(uid: String, token: String, device: Device)

    suspend fun sendMessage(body: Message): Int // status code
}