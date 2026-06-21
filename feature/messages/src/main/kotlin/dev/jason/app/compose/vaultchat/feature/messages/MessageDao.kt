package dev.jason.app.compose.vaultchat.feature.messages

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {

    @Insert
    suspend fun addMessage(messageEntity: MessageEntity)

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()

    @Query(
        "DELETE FROM messages WHERE " +
                "(`from` = :currentUserUid AND `to` = :otherUserUid) OR " +
                "(`from` = :otherUserUid AND `to` = :currentUserUid)"
    )
    suspend fun deleteMessageHistory(currentUserUid: String, otherUserUid: String)

    @Query(
        "SELECT * FROM messages WHERE " +
                "(`from` = :currentUserUid AND `to` = :otherUserUid) OR " +
                "(`from` = :otherUserUid AND `to` = :currentUserUid) " +
                "ORDER BY `timestamp` ASC"
    )
    fun getMessages(currentUserUid: String, otherUserUid: String): PagingSource<Int, MessageEntity>
}