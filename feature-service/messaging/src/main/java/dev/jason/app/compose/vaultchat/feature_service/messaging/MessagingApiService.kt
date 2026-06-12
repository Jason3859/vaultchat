package dev.jason.app.compose.vaultchat.feature_service.messaging

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.Message

class MessagingApiService(
    private val repository: MessagingApiRepository
) {

    suspend fun sendMessage(to: String, message: Message) {
        try {
            repository.sendMessage(to, message)
        } catch (e: Exception) {
            Log.e("MessagingApiService", "sendMessage: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }
}