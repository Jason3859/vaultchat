package dev.jason.app.compose.vaultchat.core

sealed interface NavEvent {

    data class NavigateToMessagingScreen(val uid: String) : NavEvent
    data object NavigateToHomeScreen : NavEvent
}