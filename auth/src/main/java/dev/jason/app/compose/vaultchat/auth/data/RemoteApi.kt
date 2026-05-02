package dev.jason.app.compose.vaultchat.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val BASE_URL = "http://10.0.2.2:8080"

interface RemoteApi {
    suspend fun registerUser(body: UserDto)
}

class KtorRemoteApi(private val httpClient: HttpClient) : RemoteApi {
    override suspend fun registerUser(body: UserDto) {
        httpClient.post("$BASE_URL/user/register") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}