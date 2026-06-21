package dev.jason.app.compose.vaultchat.ui.main.concrete.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {

    @Serializable
    data object Home : Route

    @Serializable
    data class Messaging(val uid: String) : Route

    @Serializable
    data object Profile : Route
}