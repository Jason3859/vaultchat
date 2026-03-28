package dev.jason.app.compose.vaultchat.core.local_storage.messages.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(message: MessageEntity)
}