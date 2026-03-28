package dev.jason.app.compose.vaultchat.core.messaging.ui.screen.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Home : Route {

        @Serializable
        data object Main : Route

        @Serializable
        data object Search : Route

        @Serializable
        data object Profile : Route
    }

    @Serializable
    data class Messaging(val uid: String, val displayName: String, val profilePictureUrl: String) : Route
}