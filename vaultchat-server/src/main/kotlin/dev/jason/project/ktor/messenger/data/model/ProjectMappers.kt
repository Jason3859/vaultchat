package dev.jason.project.ktor.messenger.data.model

import com.google.firebase.auth.FirebaseAuth
import dev.jason.project.ktor.messenger.domain.model.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun Message.toDto(): MessageDto = MessageDto(
    id = id,
    roomId = roomId,
    sender = getUserDtoFromFirebaseUserUid(senderUid),
    message = message,
    timestamp = timestamp.toLong()
)

fun getUserDtoFromFirebaseUserUid(uid: String): UserDto {
    val user = FirebaseAuth.getInstance().getUser(uid)

    return UserDto(user.displayName, user.photoUrl)
}

fun LocalDateTime.toLong(): Long {
    return this.atZone(ZoneId.systemDefault()).toEpochSecond()
}

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}