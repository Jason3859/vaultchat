package dev.jason.app.compose.vaultchat.core.model.user

import dev.jason.app.compose.vaultchat.core.model.device.DeviceDto
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val device: DeviceDto
)