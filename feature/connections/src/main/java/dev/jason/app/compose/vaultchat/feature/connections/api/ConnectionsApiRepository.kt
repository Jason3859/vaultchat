package dev.jason.app.compose.vaultchat.feature.connections.api

import dev.jason.app.compose.vaultchat.core.model.user.User

interface ConnectionsApiRepository {

    suspend fun getConnections(): List<User>
}