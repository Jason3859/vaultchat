package dev.jason.app.compose.vaultchat.local_storage.messages

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.domain.Message
import java.time.LocalDateTime

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val from: String,
    val to: String,
    val text: String,
    val at: String
) {
    fun toDomain() = Message(from, to, text, LocalDateTime.parse(at))
}

fun Message.toDbEntity() = MessageEntity(
    from = from,
    to = to,
    text = text,
    at = timestamp.toString()
)