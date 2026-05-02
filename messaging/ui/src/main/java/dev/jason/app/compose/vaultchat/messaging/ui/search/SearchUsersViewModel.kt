package dev.jason.app.compose.vaultchat.messaging.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(private val apiRepository: RemoteApiRepository) : ViewModel() {

    data class UiState(
        val expanded: Boolean = false,
        val searchQuery: String = "",
        val searchResults: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val hasRequested: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateState(state: UiState) {
        _uiState.update { state }
    }

    fun getAndUpdateUsers() {
        viewModelScope.launch {
            updateState(_uiState.value.copy(isLoading = true))
            val searchResults = apiRepository.searchUsers(_uiState.value.searchQuery, Firebase.auth.currentUser!!.uid)
            updateState(
                _uiState.value.copy(
                    isLoading = false,
                    hasRequested = true,
                    searchResults = searchResults
                )
            )
        }
    }
}