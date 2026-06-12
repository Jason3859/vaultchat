package dev.jason.app.compose.vaultchat.feature_service.connections.api

import dev.jason.app.compose.vaultchat.core.model.User

interface ConnectionsApiRepository {

    suspend fun getConnections(): List<User>
}