package dev.jason.app.compose.core.messaging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object SnackbarController {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val channel = Channel<String>()
    val flow = channel.receiveAsFlow()

    fun showSnackbar(string: String) {
        coroutineScope.launch {
            channel.send(string)
        }
    }
}