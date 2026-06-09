package dev.jason.app.compose.vaultchat.messaging.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Home : Route

    @Serializable
    data class Profile(val showCurrentUserProfile: Boolean) : Route

    @Serializable
    data object Loading : Route

    @Serializable
    data class Messaging(val uid: String) : Route
}