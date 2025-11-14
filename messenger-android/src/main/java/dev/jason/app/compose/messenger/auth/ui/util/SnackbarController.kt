package dev.jason.app.compose.messenger.auth.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object SnackbarController {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val _events = Channel<String>()
    val events = _events.receiveAsFlow()

    fun sendEvent(event: String) {
        coroutineScope.launch {
            _events.send(event)
        }
    }
}