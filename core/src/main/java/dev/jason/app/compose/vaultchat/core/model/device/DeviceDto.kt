package dev.jason.app.compose.vaultchat.core.model.device

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

        fun toDomain() = when (this) {
            Android -> Device.Os.Android
        }

        companion object {
            fun fromDomain(os: Device.Os) = when (os) {
                Device.Os.Android -> Android
            }
        }
    }

    @Serializable
    enum class Type {
        Mobile, Tablet;

        fun toDomain() = when (this) {
            Mobile -> Device.Type.Mobile
            Tablet -> Device.Type.Tablet
        }

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

fun DeviceDto.toDevice() = Device(
    ownerUid = ownerUid,
    name = name,
    type = type.toDomain(),
    os = os.toDomain(),
    version = version,
    token = token
)