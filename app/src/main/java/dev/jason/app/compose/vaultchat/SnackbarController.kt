package dev.jason.app.compose.vaultchat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object SnackbarController {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val flow = MutableSharedFlow<String?>()
    val events = flow.asSharedFlow()

    fun sendEvent(string: String) {
        coroutineScope.launch {
            flow.emit(string)
        }
    }
}