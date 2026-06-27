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
        // for the viewmodel if user exists inside database
        fun emptyUser(): UserUi {
            return UserUi(
                uid = "empty",
                displayName = "empty",
                profilePictureUrl = "empty",
                status = Status.Online
            )
        }
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