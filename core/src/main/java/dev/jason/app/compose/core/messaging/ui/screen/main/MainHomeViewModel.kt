package dev.jason.app.compose.core.messaging.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.core.messaging.domain.model.User
import dev.jason.app.compose.core.messaging.domain.remote.RemoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainHomeViewModel(private val remoteApi: RemoteApi) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    private val _connections = MutableStateFlow<List<User>>(emptyList())
    val connections = _connections.asStateFlow()
        .onStart { updateConnections() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val currentUserUid by lazy { Firebase.auth.currentUser?.uid!! }

    fun updateConnections() {
        _isLoading.update { true }
        viewModelScope.launch {
            val connections = remoteApi.getConnections(currentUserUid)

            _connections.update { connections }
            _isLoading.update { false }
        }
    }
}