package dev.jason.app.compose.vaultchat.feature.messaging

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MessagingApiRepoImpl(
    private val client: HttpClient
) : MessagingApiRepository {

    override suspend fun sendMessage(message: Message): StatusCode {
        val response = client.post("$BASE_URL/messaging/send") {
            contentType(ContentType.Application.Json)
            setBody(message.toDto())
        }

        return response.status.value
    }
}