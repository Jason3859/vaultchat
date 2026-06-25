package dev.jason.app.compose.vaultchat.core.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val device: DeviceDto
)