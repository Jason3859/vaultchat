package dev.jason.app.compose.vaultchat.local_storage.messages

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {
            return INSTANCE ?: Room.databaseBuilder(context, MessageDatabase::class.java, "messages.db")
                .fallbackToDestructiveMigration(false)
                .build()
                .also { INSTANCE = it }
        }
    }
}