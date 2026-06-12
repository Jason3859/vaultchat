package dev.jason.app.compose.vaultchat.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    val ownerUid: String,
    val name: String,
    val type: Type,
    val os: OS,
    val version: String,
    val token: String,
) {
    @Serializable
    enum class OS {
        Android;

        companion object {
            fun fromDomain(os: Device.Os) = when (os) {
                Device.Os.Android -> Android
            }
        }
    }

    @Serializable
    enum class Type {
        Mobile, Tablet;

        companion object {
            fun fromDomain(type: Device.Type) = when (type) {
                Device.Type.Mobile -> Mobile
                Device.Type.Tablet -> Tablet
            }
        }
    }
}

fun Device.toDto() = DeviceDto(
    ownerUid = ownerUid,
    name = name,
    type = DeviceDto.Type.fromDomain(type),
    os = DeviceDto.OS.fromDomain(os),
    version = version,
    token = token
)