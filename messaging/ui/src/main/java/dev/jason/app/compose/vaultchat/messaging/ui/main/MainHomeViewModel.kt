package dev.jason.app.compose.vaultchat.messaging.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.messaging.domain.MessagingState
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainHomeViewModel(
    private val apiRepository: RemoteApiRepository,
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    val connections = MessagingState.connections
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
            val connections = apiRepository.fetchConnections(currentUserUid)
            localStorageRepository.addAllConnections(connections)
            MessagingState.updateConnections(localStorageRepository.getConnections().first())
            _isLoading.update { false }
        }
    }
}