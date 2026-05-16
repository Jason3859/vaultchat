package dev.jason.app.compose.vaultchat.local_storage.repo.connections

import dev.jason.app.compose.vaultchat.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ConnectionsRepository {

    suspend fun addConnection(user: User)
    suspend fun addAllConnections(users: List<User>)

    fun getConnections(): Flow<List<User>>
}