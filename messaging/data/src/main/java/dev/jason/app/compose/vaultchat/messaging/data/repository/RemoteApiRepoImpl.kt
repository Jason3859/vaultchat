package dev.jason.app.compose.vaultchat.messaging.data.repository

import android.util.Log
import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.messaging.data.dto.toDto
import dev.jason.app.compose.vaultchat.messaging.data.remote.FcmApi
import dev.jason.app.compose.vaultchat.messaging.domain.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult
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
            ApiResult(response.result, response.data?.map(UserDto::toDomain))
        } catch (e: Exception) {
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            Log.e("RemoteApiImpl", "searchUsers: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun fetchConnections(uid: String): ApiResult<List<User>> {
        return try {
            api.getConnections(uid).run {
                ApiResult(result, data?.map(UserDto::toDomain))
            }
        } catch (e: Exception) {
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            Log.e("RemoteApiImpl", "getConnections: exception", e)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun updateStatus(uid: String, status: User.Status): ApiResult<Void> {
        return try {
            api.updateStatus(uid, status)
        } catch (e: Exception) {
            Log.e("RemoteApiImpl", "updateStatus: exception", e)
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun updateToken(uid: String, token: String, device: Device) {
        try {
            api.updateToken(uid, token, UserDto.DeviceDto.fromDomain(device))
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "updateToken: exception", e)
        }
    }

    override suspend fun fetchDevices(uid: String): ApiResult<List<Device>> {
        return try {
            api.fetchDevices(uid).let {
                ApiResult(it.result, it.data?.map(UserDto.DeviceDto::toDomain))
            }
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "getDevices: exception", e)
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun block(uid: String, uidToBlock: String): ApiResult<Void> {
        return try {
            api.block(uid, uidToBlock)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "block: exception", e)
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun unblock(uid: String, uidToUnblock: String): ApiResult<Void> {
        return try {
            api.unblock(uid, uidToUnblock)
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "unblock: exception", e)
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            ApiResult(ApiResult.Result.InternalError)
        }
    }

    override suspend fun fetchBlocklist(uid: String): ApiResult<List<User>> {
        return try {
            api.fetchBlocklist(uid).let { ApiResult(it.result, it.data?.map(UserDto::toDomain)) }
        } catch (e: Exception) {
            Log.e("RemoteApiRepoImpl", "fetchBlocklist: exception", e)
            e.localizedMessage?.let(SnackbarController::showSnackbar)
            ApiResult(ApiResult.Result.InternalError)
        }
    }
}