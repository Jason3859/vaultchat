package dev.jason.app.compose.vaultchat.core.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status,
    val device: DeviceDto? = null
) {
    @Serializable
    enum class Status {
        Offline, Online
    }
}

fun UserDto.Status.toStatus() = when (this) {
    UserDto.Status.Offline -> User.Status.Offline
    UserDto.Status.Online -> User.Status.Online
}

fun User.Status.toStatus() = when (this) {
    User.Status.Online -> UserDto.Status.Online
    User.Status.Offline -> UserDto.Status.Offline
}

fun UserDto.toUser() = User(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status.toStatus()
)

fun User.toDto(device: DeviceDto? = null) = UserDto(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status.toStatus(),
    device = device
)