package dev.jason.app.compose.vaultchat.messaging.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _navEvents = MutableSharedFlow<Intent>(extraBufferCapacity = 1)
    val navEvents = _navEvents.asSharedFlow()

    fun emitNavEvent(intent: Intent) {
        viewModelScope.launch {
            _navEvents.tryEmit(intent)
        }
    }
}