package dev.jason.app.compose.vaultchat.core.domain

data class User(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val devices: List<Device>,
    val status: Status
) {
    enum class Status {
        Online, Offline, Away
    }

    data class Device(
        val name: String,
        val type: Type,
        val version: String,
        val fcmToken: String,
    ) {
        enum class Type {
            Mobile, Tablet
        }
    }
}