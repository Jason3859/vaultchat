package dev.jason.app.compose.vaultchat.messaging.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.MessagingState
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainHomeViewModel(private val apiRepository: RemoteApiRepository) :
    ViewModel(CoroutineScope(Dispatchers.IO)) {

    private val _connections = MutableStateFlow<List<User>>(emptyList())
    val connections = _connections.asStateFlow()
        .onStart {
            updateConnections()
        }
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
            val connections = apiRepository.getConnections(currentUserUid)

            _connections.update { connections.data ?: emptyList() }
            _isLoading.update { false }
        }

        viewModelScope.launch {
            MessagingState.connectionsStatus.collect { list ->
                _connections.update { connections ->
                    connections.map { user ->
                        list.find { it.uid == user.uid } // find for user in list
                            ?.let { user.copy(status = it.status) }  // update status
                            ?: user // return user if not found
                    }
                }
            }
        }
    }
}