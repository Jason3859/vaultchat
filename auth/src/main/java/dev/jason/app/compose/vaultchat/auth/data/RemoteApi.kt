package dev.jason.app.compose.vaultchat.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RemoteApi {
    suspend fun registerUser(body: UserDto): Int
    suspend fun addDevice(body: UserDto.DeviceDto)
}

class KtorRemoteApi(private val httpClient: HttpClient, private val baseUrl: String) : RemoteApi {

    // TODO: remove logs

    override suspend fun registerUser(body: UserDto): Int {
        val response = httpClient.post("$baseUrl/user/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        return response.status.value
    }

    override suspend fun addDevice(body: UserDto.DeviceDto) {
        httpClient.post("$baseUrl/device/add") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}