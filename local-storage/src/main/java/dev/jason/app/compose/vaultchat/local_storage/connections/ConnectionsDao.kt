package dev.jason.app.compose.vaultchat.local_storage.connections

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionsDao {

    @Insert
    suspend fun saveConnection(connectionsEntity: ConnectionsEntity)

    @Upsert
    suspend fun saveAllConnections(connections: List<ConnectionsEntity>)

    @Query("SELECT * FROM connections")
    fun getAllConnections(): Flow<List<ConnectionsEntity>>
}