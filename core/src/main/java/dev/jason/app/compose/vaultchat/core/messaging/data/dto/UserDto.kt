package dev.jason.app.compose.vaultchat.core.messaging.data.dto

import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String
)

fun UserDto.toDomain() = User(uid, displayName, profilePictureUrl)