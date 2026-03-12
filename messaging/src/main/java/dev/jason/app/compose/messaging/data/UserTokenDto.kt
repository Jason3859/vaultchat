package dev.jason.app.compose.messaging.data

import dev.jason.app.compose.messaging.domain.UserToken
import kotlinx.serialization.Serializable

@Serializable
data class UserTokenDto(
    val uid: String,
    val token: String
)

fun UserToken.toDto() = UserTokenDto(uid, token)