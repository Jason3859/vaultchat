package dev.jason.app.compose.vaultchat.messaging.data.dto

import dev.jason.app.compose.vaultchat.core.domain.Device
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
    fun toDomain() =
        User(uid, displayName, profilePictureUrl, devices.map(DeviceDto::toDomain), status)

    @Serializable
    data class DeviceDto(
        val name: String,
        val type: Device.Type,
        val os: String,
        val version: String,
        val fcmToken: String,
    ) {
        fun toDomain() = Device(name, type, Device.Os.valueOf(os), version, fcmToken)

        companion object {
            fun fromDomain(device: Device) =
                DeviceDto(device.name, device.type, "Android", device.version, device.fcmToken)
        }
    }
}