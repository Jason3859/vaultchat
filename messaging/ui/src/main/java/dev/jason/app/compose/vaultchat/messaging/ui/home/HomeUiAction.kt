package dev.jason.app.compose.vaultchat.messaging.ui.home

sealed interface HomeUiAction {

    data object Search : HomeUiAction
    data class OnUiStateChange(val state: (HomeUiState) -> HomeUiState) : HomeUiAction
}