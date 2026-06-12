package dev.jason.app.compose.vaultchat.feature_service.logout

import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters

internal class LogoutRemoteRepoImpl(
    private val baseUrl: String,
    private val client: HttpClient
) : LogoutRemoteRepository {

    override suspend fun beginLogout(
        device: Device,
        clearMessages: Boolean,
        onLogoutSuccessful: () -> Unit
    ): Int {
        val response = client.delete("$baseUrl/user/logout") {
            contentType(ContentType.Application.Json)
            setBody(device.toDto())
            parameters {
                append("clearMessages", clearMessages.toString())
            }
        }

        return response.status.value.also { onLogoutSuccessful.invoke() }
    }
}