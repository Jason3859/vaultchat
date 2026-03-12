package dev.jason.app.compose.core.messaging.data.dto

import dev.jason.app.compose.core.messaging.domain.model.UserToken
import kotlinx.serialization.Serializable

@Serializable
data class UserTokenDto(
    val uid: String,
    val token: String
)

fun UserToken.toDto() = UserTokenDto(uid, token)