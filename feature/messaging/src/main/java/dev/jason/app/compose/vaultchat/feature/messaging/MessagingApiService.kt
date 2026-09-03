package dev.jason.app.compose.vaultchat.feature.messaging

import android.util.Log
import dev.jason.app.compose.vaultchat.core.model.message.Message

class MessagingApiService(
    private val repository: MessagingApiRepository
) {

    suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            repository.sendMessage(message)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("MessagingApiService", "sendMessage: failed to send message", e)
            Result.failure(e)
        }
    }
}