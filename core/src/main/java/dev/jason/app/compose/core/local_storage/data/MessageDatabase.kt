package dev.jason.app.compose.core.local_storage.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

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