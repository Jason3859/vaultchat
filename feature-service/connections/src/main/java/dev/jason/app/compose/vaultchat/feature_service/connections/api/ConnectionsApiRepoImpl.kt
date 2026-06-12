package dev.jason.app.compose.vaultchat.feature_service.connections.api

import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.UserDto
import dev.jason.app.compose.vaultchat.core.model.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

class ConnectionsApiRepoImpl(
    private val baseUrl: String,
    private val client: HttpClient
) : ConnectionsApiRepository {

    private val currentUserUid by lazy {
        AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null")
    }

    override suspend fun getConnections(): List<User> {
        return client.get("$baseUrl/social/connections") {
            parameters { append("uid", currentUserUid) }
        }
            .body<List<UserDto>>()
            .map(UserDto::toUser)
    }
}