package dev.jason.app.compose.vaultchat.feature.connections.db

import dev.jason.app.compose.vaultchat.core.model.user.User
import kotlinx.coroutines.flow.Flow

interface ConnectionsDbRepository {

    fun getConnections(): Flow<List<User>>
    suspend fun getConnection(uid: String): User?
    suspend fun updateConnections(connections: List<User>)
    suspend fun updateStatus(uid: String, status: User.Status)
}