package dev.jason.app.compose.vaultchat.feature.messages

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class MessageDatabase : RoomDatabase() {

    abstract val dao: MessageDao

    companion object {
        @Volatile
        private var INSTANCE: MessageDatabase? = null
        
        fun getInstance(context: Context): MessageDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context = context,
                klass = MessageDatabase::class.java,
                name = "messages"
            )
                .fallbackToDestructiveMigration(true)
                .build()
                .also { INSTANCE = it }
        }
    }
}