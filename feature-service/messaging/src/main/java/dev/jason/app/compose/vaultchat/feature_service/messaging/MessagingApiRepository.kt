package dev.jason.app.compose.vaultchat.feature_service.messaging

import dev.jason.app.compose.vaultchat.core.model.Message

interface MessagingApiRepository {

    suspend fun sendMessage(userUid: String, message: Message)
}