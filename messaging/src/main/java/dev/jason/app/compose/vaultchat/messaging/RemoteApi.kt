package dev.jason.app.compose.vaultchat.messaging

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface RemoteApi {
    suspend fun heartbeat(uid: String)
}

class KtorRemoteApi(private val httpClient: HttpClient, private val baseUrl: String) : RemoteApi {
    override suspend fun heartbeat(uid: String) {
        httpClient.patch("$baseUrl/user/heartbeat") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
            }
        }
    }
}