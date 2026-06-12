package dev.jason.app.compose.vaultchat.feature_service.messaging

import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MessagingApiRepoImpl(
    private val baseUrl: String,
    private val client: HttpClient
) : MessagingApiRepository {

    override suspend fun sendMessage(
        userUid: String,
        message: Message
    ) {
        client.post("$baseUrl/messaging/send") {
            contentType(ContentType.Application.Json)
            setBody(message.toDto())
        }
    }
}