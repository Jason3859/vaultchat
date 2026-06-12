package dev.jason.app.compose.vaultchat.feature_service.messages

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.vaultchat.core.model.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(tableName = "messages")
data class MessageEntity(
    val from: String,
    val to: String,
    val text: String,
    @PrimaryKey val timestamp: Long
)

fun MessageEntity.toMessage() = Message(
    from = from,
    to = to,
    text = text,
    timestamp = timestamp.toLocalDateTime()
)

fun Message.toEntity() = MessageEntity(
    from = from,
    to = to,
    text = text,
    timestamp = timestamp.toLong()
)

fun LocalDateTime.toLong() = this
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()