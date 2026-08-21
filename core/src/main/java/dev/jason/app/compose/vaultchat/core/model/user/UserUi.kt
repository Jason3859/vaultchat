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
    /**
     * For known users. i.e., for the users that are already connected.
     * This constructor is used by navigation and is being passed to
     * [MessagingViewModel] for fetching the user from database
     */
    constructor(uid: String) : this(uid, "null", "null", Status.Offline)

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