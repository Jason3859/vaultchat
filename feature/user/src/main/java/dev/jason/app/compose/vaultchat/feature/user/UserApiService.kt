package dev.jason.app.compose.vaultchat.feature.user

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.User

class UserApiService(
    private val repository: UserApiRepository
) {

    suspend fun heartbeat(uid: String) {
        try {
            repository.heartbeat(uid)
        } catch (e: Exception) {
            Log.e("UserApiService", "heartbeat: exception", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun registerUser(dto: RegisterUserDto) {
        try {
            repository.registerUser(dto)
        } catch (e: Exception) {
            Log.e("UserApiService", "registerUser: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun searchUsers(query: String): List<User> {
        return try {
            repository.searchUser(query)
        } catch (e: Exception) {
            Log.e("UserApiService", "searchUsers: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }
}