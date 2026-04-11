package dev.jason.app.compose.vaultchat.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val device: DeviceDto
) {
    @Serializable
    data class DeviceDto(
        val name: String,
        val os: String = "Android",
        val type: Type,
        val version: String,
        val fcmToken: String,
    ) {
        enum class Type {
            Mobile, Tablet
        }
    }
}