package dev.jason.app.compose.vaultchat.feature.messaging

import dev.jason.app.compose.vaultchat.core.model.message.Message

interface MessagingApiRepository {

    suspend fun sendMessage(message: Message): StatusCode
}