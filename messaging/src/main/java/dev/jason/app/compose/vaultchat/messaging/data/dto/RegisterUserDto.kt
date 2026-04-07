package dev.jason.app.compose.vaultchat.messaging.data.dto

import dev.jason.app.compose.vaultchat.messaging.domain.model.RegisterUser
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val fcmToken: String
)

fun RegisterUser.toDto() = RegisterUserDto(uid, displayName, profilePictureUrl, token)