package dev.jason.app.compose.vaultchat.messaging

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.contentType

private const val BASE_URL = "http://10.0.2.2:8080"

interface RemoteApi {
    suspend fun heartbeat(uid: String)
}

class KtorRemoteApi(private val httpClient: HttpClient) : RemoteApi {
    override suspend fun heartbeat(uid: String) {
        httpClient.patch("$BASE_URL/user/heartbeat") {
            contentType(ContentType.Application.Json)
            url {
                parameters.append("uid", uid)
            }
        }
    }
}