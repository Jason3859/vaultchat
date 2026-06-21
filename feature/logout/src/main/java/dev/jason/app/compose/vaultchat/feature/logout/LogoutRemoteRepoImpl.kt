package dev.jason.app.compose.vaultchat.feature.logout

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class LogoutRemoteRepoImpl(
    private val client: HttpClient
) : LogoutRemoteRepository {

    override suspend fun beginLogout(
        device: Device,
        clearMessages: Boolean,
        onLogoutSuccessful: () -> Unit
    ): Int {
        val response = client.delete("$BASE_URL/user/logout") {
            contentType(ContentType.Application.Json)
            setBody(device.toDto())
            parameter("clearMessages", clearMessages.toString())
        }

        return response.status.value.also { onLogoutSuccessful.invoke() }
    }
}