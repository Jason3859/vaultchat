package dev.jason.app.compose.vaultchat.feature.connections.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionsDao {

    @Upsert
    suspend fun saveAllConnections(connections: List<ConnectionsEntity>)

    @Query("UPDATE connections SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: User.Status)

    @Query("SELECT * FROM connections")
    fun getAllConnections(): Flow<List<ConnectionsEntity>>

    @Query("SELECT * FROM connections WHERE id = :id")
    fun getById(id: String): Flow<ConnectionsEntity>
}