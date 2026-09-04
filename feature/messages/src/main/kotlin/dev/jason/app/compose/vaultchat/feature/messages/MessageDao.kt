package dev.jason.app.compose.vaultchat.feature.messages

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MessageDao {

    @Upsert
    suspend fun addMessage(messageEntity: MessageEntity)

    @Upsert
    fun addMessages(messages: List<MessageEntity>)

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