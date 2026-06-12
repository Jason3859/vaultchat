package dev.jason.app.compose.vaultchat.feature_service.connections.db

import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConnectionsDbRepoImpl(private val dao: ConnectionsDao) : ConnectionsDbRepository {
    override fun getConnections(): Flow<List<User>> {
        return dao.getAllConnections()
            .map { list ->
                list.map { entity ->
                    entity.toUser()
                }
            }
    }

    override suspend fun updateConnections(connections: List<User>) {
        dao.saveAllConnections(
            connections = connections.map(User::toEntity)
        )
    }

    override suspend fun updateStatus(uid: String, status: User.Status) {
        dao.updateStatus(uid, status)
    }
}