package dev.jason.app.compose.vaultchat.feature_service.connections.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ConnectionsEntity::class], version = 1, exportSchema = false)
abstract class ConnectionsDatabase : RoomDatabase() {

    abstract fun dao(): ConnectionsDao

    companion object {
        @Volatile
        private var INSTANCE: ConnectionsDatabase? = null

        fun getDatabase(context: Context): ConnectionsDatabase {
            return INSTANCE ?: Room.databaseBuilder(context, ConnectionsDatabase::class.java, "connections")
                .fallbackToDestructiveMigration(false)
                .build()
                .also { INSTANCE = it }
        }
    }
}