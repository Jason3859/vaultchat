package dev.jason.app.compose.vaultchat

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object SnackbarController {

    private val flow = MutableStateFlow<String?>(null)
    val events = flow.asStateFlow()

    fun sendEvent(string: String) {
        flow.update { string }
    }
}