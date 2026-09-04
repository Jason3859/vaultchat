package dev.jason.app.compose.vaultchat.feature.messaging

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.message.Message
import io.ktor.serialization.ContentConvertException

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

    suspend fun fetchMessages(): List<Message> {
        return try {
            repository.fetchMessages()
        } catch (e: ContentConvertException) {
            Log.e("MessagingApiService", "fetchMessages: content convert exception", e.cause)
            emptyList()
        } catch (exception: Exception) {
            Log.e(
                "MessagingApiService",
                "fetchMessages: exception while fetching messages",
                exception
            )
            ToastController.showToast("Error occurred while loading messages")
            emptyList()
        }
    }
}