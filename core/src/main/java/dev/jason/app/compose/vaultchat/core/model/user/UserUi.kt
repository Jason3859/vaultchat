package dev.jason.app.compose.vaultchat.core.model.user

import androidx.compose.runtime.Immutable
import dev.jason.app.compose.vaultchat.core.model.user.User.Status

@Immutable
data class UserUi(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val status: Status
) {
    companion object {
        fun emptyUser(): UserUi {
            return UserUi(
                uid = "empty",
                displayName = "empty",
                profilePictureUrl = "empty",
                status = Status.Online
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is UserUi) return false
        if (this.uid != other.uid) return false
        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + profilePictureUrl.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}

fun User.toUi() = UserUi(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status
)

fun UserUi.toUser() = User(
    uid = uid,
    displayName = displayName,
    profilePictureUrl = profilePictureUrl,
    status = status
)