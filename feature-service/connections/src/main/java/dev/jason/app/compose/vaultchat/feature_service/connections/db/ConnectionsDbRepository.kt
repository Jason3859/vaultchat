package dev.jason.app.compose.vaultchat.feature_service.connections.db

import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.flow.Flow

interface ConnectionsDbRepository {

    fun getConnections(): Flow<List<User>>
    suspend fun updateConnections(connections: List<User>)
    suspend fun updateStatus(uid: String, status: User.Status)
}