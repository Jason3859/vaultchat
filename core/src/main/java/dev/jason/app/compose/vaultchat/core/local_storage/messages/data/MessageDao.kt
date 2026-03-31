package dev.jason.app.compose.vaultchat.core.local_storage.messages.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE `from` = :from OR `from` = :to")
    suspend fun getMessages(from: String, to: String): List<MessageEntity>
}