package dev.jason.app.compose.messaging.ui

data class AppUiState(
    val isEnteringToken: Boolean = true,
    val remoteToken: String = "",
    val messageText: String = ""
)
