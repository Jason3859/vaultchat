package dev.jason.app.compose.vaultchat.feature.logout

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.Device

class LogoutService(
    private val repository: LogoutRemoteRepository
) {

    suspend fun beginLogout(
        device: Device,
        clearMessages: Boolean
    ) {
        try {
            repository.beginLogout(
                device = device,
                clearMessages = clearMessages,
                onLogoutSuccessful = { Firebase.auth.signOut() }
            )
        } catch (e: Exception) {
            Log.e("LogoutService", "beginLogout: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun logoutCurrentDevice(clearMessages: Boolean) {
        val currentDevice = AppState.currentDevice.value!!
        beginLogout(currentDevice, clearMessages)
    }
}