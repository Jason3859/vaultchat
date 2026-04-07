package dev.jason.app.compose.vaultchat.local_storage.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(message: MessageEntity)

    @Query(
        "SELECT * FROM messages WHERE (`from` = :currentUserUid AND `to` = :otherUserUid) OR (`from` = :otherUserUid AND `to` = :currentUserUid)"
    )
    fun getMessages(currentUserUid: String, otherUserUid: String): Flow<List<MessageEntity>>
}