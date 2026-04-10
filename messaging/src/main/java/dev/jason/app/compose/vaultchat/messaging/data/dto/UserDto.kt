package dev.jason.app.compose.vaultchat.messaging.data.dto

import dev.jason.app.compose.vaultchat.core.domain.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val devices: List<DeviceDto> = emptyList(), // empty list for connections
    val status: User.Status
) {
    fun toDomain() = User(uid, displayName, profilePictureUrl, devices.map(DeviceDto::toDomain), status)

    @Serializable
    data class DeviceDto(
        val name: String,
        val version: String,
        val fcmToken: String,
    ) {
        fun toDomain() = User.Device(name, version, fcmToken)
        companion object {
            fun fromDomain(device: User.Device) = DeviceDto(device.name, device.version, device.fcmToken)
        }
    }
}