package dev.jason.app.compose.vaultchat.messaging.ui

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val localStorageRepository: LocalStorageRepository) : ViewModel() {

    private val _navEvents = MutableSharedFlow<Intent>(extraBufferCapacity = 1)
    val navEvents = _navEvents.asSharedFlow()

    fun emitNavEvent(intent: Intent) {
        viewModelScope.launch {
            _navEvents.tryEmit(intent)
        }
    }

    fun getUserByUid(uid: String): Flow<User> {
        return localStorageRepository.getConnectionByUid(uid)
    }
}