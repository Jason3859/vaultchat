package dev.jason.app.compose.vaultchat.ui.concrete.auth

import dev.jason.app.compose.vaultchat.core.model.DeviceDto
import dev.jason.app.compose.vaultchat.core.model.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RemoteApiRepository {
    suspend fun registerUser(body: UserDto): Int
    suspend fun addDevice(body: DeviceDto)
}

class KtorRemoteApiRepository(private val httpClient: HttpClient, private val baseUrl: String) : RemoteApiRepository {

    override suspend fun registerUser(body: UserDto): Int {
        val response = httpClient.post("$baseUrl/user/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        return response.status.value
    }

    override suspend fun addDevice(body: DeviceDto) {
        httpClient.post("$baseUrl/device/add") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}