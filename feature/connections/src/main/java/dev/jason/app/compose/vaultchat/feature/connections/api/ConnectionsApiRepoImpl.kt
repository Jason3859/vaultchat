package dev.jason.app.compose.vaultchat.feature.connections.api

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.UserDto
import dev.jason.app.compose.vaultchat.core.model.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ConnectionsApiRepoImpl(
    private val client: HttpClient
) : ConnectionsApiRepository {

    private val currentUserUid by lazy {
        AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null")
    }

    override suspend fun getConnections(): List<User> {
        return client.get("$BASE_URL/social/connections") {
            parameter("uid", currentUserUid)
        }
            .body<List<UserDto>>()
            .map(UserDto::toUser)
    }
}