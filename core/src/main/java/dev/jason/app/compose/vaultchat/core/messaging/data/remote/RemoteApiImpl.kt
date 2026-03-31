package dev.jason.app.compose.vaultchat.core.messaging.data.remote

import android.util.Log
import dev.jason.app.compose.vaultchat.core.messaging.SnackbarController
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.toDomain
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.toDto
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.ApiResult
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.Message
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.UserToken
import dev.jason.app.compose.vaultchat.core.messaging.domain.remote.RemoteApi

class RemoteApiImpl(private val api: FcmApi) : RemoteApi {
    override suspend fun sendMessage(body: Message) {
        try {
            api.sendMessage(body.toDto()).also {
                Log.d("RemoteApiImpl", "sendMessage: response: $it")
            }
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

    override suspend fun searchUsers(name: String, from: String): ApiResult<List<User>> {
        return try {
            val response = api.searchUsers(name, from)
            ApiResult(response.result, response.data?.map(UserDto::toDomain))
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "searchUsers: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun getConnections(uid: String): ApiResult<List<User>> {
        Log.d("RemoteApiImpl", "getConnections: trying to get with uid $uid")
        return try {
            api.getConnections(uid).run {
                ApiResult(result, data?.map(UserDto::toDomain))
            }
        } catch (e: Exception) {
            SnackbarController.showSnackbar(e.localizedMessage!!)
            Log.e("RemoteApiImpl", "getConnections: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }
}