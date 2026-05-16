package dev.jason.app.compose.vaultchat.local_storage.repo.connections

import dev.jason.app.compose.vaultchat.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ConnectionsRepository {

    suspend fun addAllConnections(users: List<User>)
    suspend fun updateStatus(id: String, status: User.Status)
    fun getConnections(): Flow<List<User>>
    fun getConnectionById(id: String): Flow<User>
}