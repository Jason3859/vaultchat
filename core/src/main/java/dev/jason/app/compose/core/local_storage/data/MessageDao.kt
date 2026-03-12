package dev.jason.app.compose.core.local_storage.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(message: MessageEntity)
}