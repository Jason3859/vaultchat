package dev.jason.app.compose.vaultchat.web_socket.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface WebSocketConnectionRoute : NavKey {

    @Serializable data object HomeScreen : WebSocketConnectionRoute
    @Serializable data class MessagingScreen(val id: String) : WebSocketConnectionRoute
}