package dev.jason.app.compose.vaultchat.feature.messaging

import dev.jason.app.compose.vaultchat.core.model.Message

interface MessagingApiRepository {

    suspend fun sendMessage(message: Message): StatusCode
}