package dev.jason.app.compose.vaultchat.core.model.message

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class MessageUi(
    val from: String,
    val to: String,
    val text: String,
    val timestamp: String
)

fun Message.toUi() = MessageUi(
    from = from,
    to = to,
    text = text,
    timestamp = timestamp.asString()
)

private fun LocalDateTime.asString() = "${dayOfMonth}/${month.value}/${year % 100} ${hour}:${
    if (minute >= 10) minute else minute.toString().padStart(2, '0')
}"