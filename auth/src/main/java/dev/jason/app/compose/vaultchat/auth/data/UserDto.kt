package dev.jason.app.compose.vaultchat.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val device: DeviceDto
) {
    @Serializable
    data class DeviceDto(
        val ownerUid: String,
        val name: String,
        val type: Type,
        val os: String,
        val version: String,
        val token: String,
    ) {
        @Serializable
        enum class Type {
            Mobile, Tablet
        }
    }
}