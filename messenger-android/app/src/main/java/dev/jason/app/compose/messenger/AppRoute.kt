package dev.jason.app.compose.messenger

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    @Serializable data object Authentication : AppRoute
    @Serializable data object WebSocketConnection : AppRoute
}