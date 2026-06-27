package dev.jason.app.compose.vaultchat.feature.messaging

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.message.Message

class MessagingApiService(
    private val repository: MessagingApiRepository
) {

    suspend fun sendMessage(message: Message): StatusCode {
        try {
            return repository.sendMessage(message)
        } catch (e: Exception) {
            Log.e("MessagingApiService", "sendMessage: exception occurred", e)
            ToastController.showErrorOccurredToast()
            return 1000
        }
    }
}