package dev.jason.app.compose.vaultchat.messaging.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val apiRepository: RemoteApiRepository,
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
        .onStart {
            updateConnections()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )

    private val currentUserUid by lazy { Firebase.auth.currentUser?.uid!! }

    fun onAction(action: HomeUiAction) {
        when (action) {
            HomeUiAction.Search -> searchUsers()
            is HomeUiAction.OnUiStateChange -> _uiState.update { currentState -> action.state(currentState) }
        }
    }

    private fun updateConnections() {
        _uiState.update { it.copy(areConnectionsFetched = false) }
        viewModelScope.launch {
            val connections = apiRepository.fetchConnections(currentUserUid)
            localStorageRepository.addAllConnections(connections)
            _uiState.update { it.copy(areConnectionsFetched = true) }
        }
    }

    private fun searchUsers() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    hasRequestedSearchAtLeastOnce = true,
                    isSearchSuccessful = false
                )
            }

            val response = apiRepository.searchUsers(
                _uiState.value.searchQuery,
                currentUserUid
            )

            Log.d("HomeViewModel", "searchUsers: $response")

            _uiState.update {
                it.copy(
                    hasRequestedSearchAtLeastOnce = true,
                    isSearchSuccessful = true,
                    searchResults = response
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            localStorageRepository.getConnections().collect { list ->
                _uiState.update {
                    it.copy(
                        connections = list
                    )
                }
            }
        }
    }
}