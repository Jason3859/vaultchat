package dev.jason.app.compose.vaultchat.core.local_storage.messages.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.local_storage.messages.domain.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val from: String,
    val text: String,
    val at: String
)

fun Message.toDbEntity() = MessageEntity(
    from = from,
    text = text,
    at = at.toString()
)
