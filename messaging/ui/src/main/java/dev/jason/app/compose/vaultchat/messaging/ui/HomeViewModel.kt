package dev.jason.app.compose.vaultchat.messaging.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _navEvent = MutableStateFlow<Intent?>(null)
    val navEvent = _navEvent.asSharedFlow()

    fun emitNavEvent(intent: Intent?) {
        _navEvent.update { intent }
    }

    fun clearNavEvent() {
        _navEvent.update { null }
    }
}