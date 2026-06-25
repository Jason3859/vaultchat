package dev.jason.app.compose.vaultchat.ui.main.concrete.nav

import androidx.navigation3.runtime.NavKey
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Home : Route

    @Serializable
    data class Messaging(
        val uid: String,
        val displayName: String? = null,
        val profilePictureUrl: String? = null,
        val status: User.Status? = null
    ) : Route {
        companion object {
            fun fromUser(user: UserUi) =
                Messaging(user.uid, user.displayName, user.profilePictureUrl, user.status)
        }
    }

    @Serializable
    data object Profile : Route
}