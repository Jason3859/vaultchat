package dev.jason.app.compose.vaultchat.messaging.data.repository

import android.util.Log
import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.messaging.data.dto.toDto
import dev.jason.app.compose.vaultchat.messaging.data.remote.RemoteApi
import dev.jason.app.compose.vaultchat.messaging.domain.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException

class RemoteApiRepoImpl(private val api: RemoteApi) : RemoteApiRepository {

    override suspend fun fetchBlocklist(uid: String): List<User> {
        return try {
            api.fetchBlocklist(uid).map(UserDto::toDomain)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "fetchBlocklist: exception", e)
            handleException(e)
            emptyList()
        }
    }

    override suspend fun block(uid: String, uidToBlock: String) {
        try {
            api.block(uid, uidToBlock)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "block: exception", e)
            handleException(e)
        }
    }

    override suspend fun unblock(uid: String, uidToUnblock: String) {
        try {
            api.unblock(uid, uidToUnblock)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "unblock: exception", e)
            handleException(e)
        }
    }

    override suspend fun fetchConnections(uid: String): List<User> {
        return try {
            api.getConnections(uid).map(UserDto::toDomain)
        } catch (e: Exception) {
            SnackbarController.showSnackbar("An Internal error occurred")
            Log.e("RemoteApiRepoImpl", "fetchConnections: exception", e)
            emptyList()
        }
    }

    override suspend fun searchUsers(name: String, from: String): List<User> {
        return try {
            val response = api.searchUsers(name, from)
            response.map(UserDto::toDomain)
        } catch (e: Exception) {
            Log.e("RemoteApiImpl", "searchUsers: exception", e)
            handleException(e)
            emptyList()
        }
    }

    override suspend fun fetchDevices(uid: String): List<Device> {
        return try {
            api.fetchDevices(uid).map(UserDto.DeviceDto::toDomain)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "fetchDevices: exception", e)
            handleException(e)
            emptyList()
        }
    }

    override suspend fun updateStatus(uid: String, status: User.Status) {
        try {
            api.updateStatus(uid, status)
        } catch (e: Exception) {
            Log.e("RemoteApiImpl", "updateStatus: exception", e)
            handleException(e)
        }
    }

    override suspend fun updateToken(uid: String, token: String, device: Device) {
        try {
            api.updateToken(uid, token, UserDto.DeviceDto.fromDomain(device))
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "updateToken: exception", e)
        }
    }

    override suspend fun sendMessage(body: Message): Int {
        return try {
            api.sendMessage(body.toDto())
        } catch (e: Exception) {
            Log.e("RemoteApiImpl", "sendMessage: exception", e)
            handleException(e)
            1000 // internal error
        }
    }

    private fun handleException(e: Exception) {
        when (e) {
            is ClientRequestException -> {
                Log.e("RemoteApiRepoImpl", "Client error: ${e.response.status}", e)
                SnackbarController.showSnackbar("Request error occurred")
            }
            is ServerResponseException -> {
                Log.e("RemoteApiRepoImpl", "Server error: ${e.response.status}", e)
                SnackbarController.showSnackbar("Server error occurred")
            }
            else -> {}
        }
    }
}