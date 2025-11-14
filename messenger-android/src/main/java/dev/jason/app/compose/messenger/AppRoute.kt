package dev.jason.app.compose.messenger

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey {

    @Serializable data object Authentication : AppRoute
    @Serializable data object WebSocketConnection : AppRoute
}