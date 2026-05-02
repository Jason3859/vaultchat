package dev.jason.app.compose.vaultchat.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RemoteApi {
    suspend fun registerUser(body: UserDto)
}

class KtorRemoteApi(private val httpClient: HttpClient, private val baseUrl: String) : RemoteApi {
    override suspend fun registerUser(body: UserDto) {
        httpClient.post("$baseUrl/user/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}