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
        val ownerUid: String,
        val name: String,
        val type: Type,
        val os: OS,
        val version: String,
        val token: String,
    ) {
        constructor(
            name: String,
            type: Type,
            os: OS,
            version: String,
            token: String
        ) : this("", name, type, os, version, token)

        fun toDomain() = Device(name, type.toDomain(), os.toDomain(), version, token)

        enum class Type {
            Mobile, Tablet;

            fun toDomain() = when (this) {
                Mobile -> Device.Type.Mobile
                Tablet -> Device.Type.Tablet
            }

            companion object {
                fun fromDomain(type: Device.Type): Type {
                    return when (type) {
                        Device.Type.Mobile -> Mobile
                        Device.Type.Tablet -> Tablet
                    }
                }
            }
        }

        enum class OS {
            Android;

            fun toDomain() = when (this) {
                Android -> Device.Os.Android
            }

            companion object {
                fun fromDomain(os: Device.Os) = when (os) {
                    Device.Os.Android -> Android
                }
            }
        }

        companion object {
            fun fromDomain(device: Device) =
                DeviceDto(device.name, Type.fromDomain(device.type), OS.fromDomain(device.os), device.version, device.token)

            fun fromDomain(name: String, device: Device) =
                DeviceDto(name, device.name, Type.fromDomain(device.type), OS.fromDomain(device.os), device.version, device.token)
        }
    }
}