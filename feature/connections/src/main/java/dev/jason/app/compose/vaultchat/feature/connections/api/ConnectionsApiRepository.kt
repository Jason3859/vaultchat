package dev.jason.app.compose.vaultchat.feature.connections.api

import dev.jason.app.compose.vaultchat.core.model.User

interface ConnectionsApiRepository {

    suspend fun getConnections(): List<User>
}