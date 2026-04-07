package dev.jason.app.compose.vaultchat.messaging.data.repository

import android.util.Log
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.messaging.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.data.dto.toDomain
import dev.jason.app.compose.vaultchat.messaging.data.dto.toDto
import dev.jason.app.compose.vaultchat.messaging.data.remote.FcmApi
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult
import dev.jason.app.compose.vaultchat.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository

class RemoteApiRepoImpl(private val api: FcmApi) : RemoteApiRepository {
    override suspend fun sendMessage(body: Message): ApiResult<Void> {
        return try {
            api.sendMessage(body.toDto())
        } catch (e: Exception) {
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            Log.e("RemoteApiImpl", "sendMessage: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun searchUsers(name: String, from: String): ApiResult<List<User>> {
        return try {
            val response = api.searchUsers(name, from)
            ApiResult(response.result, response.data?.map(dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto::toDomain))
        } catch (e: Exception) {
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            Log.e("RemoteApiImpl", "searchUsers: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun getConnections(uid: String): ApiResult<List<User>> {
        return try {
            api.getConnections(uid).run {
                ApiResult(result, data?.map(dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto::toDomain))
            }
        } catch (e: Exception) {
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            Log.e("RemoteApiImpl", "getConnections: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun registerUser(body: dev.jason.app.compose.vaultchat.messaging.domain.model.RegisterUser) {
        return try {
            api.registerUser(body.toDto())
        } catch (e: Exception) {
            Log.e("RemoteApiImpl", "registerUser: exception", e)
            SnackbarController.showSnackbar(e.localizedMessage ?: return)
        }
    }
}