package dev.jason.app.compose.vaultchat.auth.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.Auth)
    val currentScreen = _currentScreen.asStateFlow()

    fun updateCurrentScreen(screen: Screen) {
        _currentScreen.update { screen }
    }

    enum class Screen {
        Auth, Loading
    }
}