package dev.jason.app.compose.core.messaging.data.remote

import android.util.Log
import dev.jason.app.compose.core.messaging.SnackbarController
import dev.jason.app.compose.core.messaging.data.dto.UserDto
import dev.jason.app.compose.core.messaging.data.dto.toDomain
import dev.jason.app.compose.core.messaging.data.dto.toDto
import dev.jason.app.compose.core.messaging.domain.model.Message
import dev.jason.app.compose.core.messaging.domain.model.User
import dev.jason.app.compose.core.messaging.domain.model.UserToken
import dev.jason.app.compose.core.messaging.domain.remote.RemoteApi

class RemoteApiImpl(private val api: FcmApi) : RemoteApi {
    override suspend fun sendMessage(body: Message) {
        try {
            api.sendMessage(body.toDto())
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "sendMessage: exception", e)
        }
    }

    override suspend fun updateFcmToken(body: UserToken) {
        try {
            api.updateFcmToken(body.toDto())
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "updateFcmToken: exception", e)
        }
    }

    override suspend fun searchUsers(name: String, from: String): List<User> {
        return try {
            api.searchUsers(name, from).map(UserDto::toDomain)
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "searchUsers: exception", e)
            emptyList()
        }
    }

    override suspend fun getConnections(uid: String): List<User> {
        return try {
            api.getConnections(uid).map { it.toDomain() }
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "getConnections: exception", e)
            emptyList()
        }
    }
}