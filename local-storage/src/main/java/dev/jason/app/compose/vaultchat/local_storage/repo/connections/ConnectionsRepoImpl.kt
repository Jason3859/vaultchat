package dev.jason.app.compose.vaultchat.local_storage.repo.connections

import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.local_storage.connections.ConnectionsDao
import dev.jason.app.compose.vaultchat.local_storage.connections.ConnectionsEntity
import dev.jason.app.compose.vaultchat.local_storage.connections.toEntity
import dev.jason.app.compose.vaultchat.local_storage.connections.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConnectionsRepoImpl(private val dao: ConnectionsDao) : ConnectionsRepository {

    override suspend fun addAllConnections(users: List<User>) {
        dao.saveAllConnections(users.map(User::toEntity))
    }

    override suspend fun updateStatus(id: String, status: User.Status) {
        dao.updateStatus(id, status)
    }

    override fun getConnections(): Flow<List<User>> {
        return dao.getAllConnections().map { it.map(ConnectionsEntity::toUser) }
    }

    override fun getConnectionById(id: String): Flow<User> {
        return dao.getById(id).map(ConnectionsEntity::toUser)
    }
}