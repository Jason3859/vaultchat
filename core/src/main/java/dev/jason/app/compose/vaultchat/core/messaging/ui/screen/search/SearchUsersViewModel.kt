package dev.jason.app.compose.vaultchat.core.messaging.ui.screen.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.core.messaging.domain.remote.RemoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchUsersViewModel(private val remoteApi: RemoteApi) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    data class UiState(
        val expanded: Boolean = false,
        val searchQuery: String = "",
        val searchResults: List<User> = emptyList(),
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateState(state: UiState) {
        _uiState.update { state }
    }

    fun getAndUpdateUsers() {
        viewModelScope.launch {
            updateState(_uiState.value.copy(isLoading = true))
            val response = remoteApi.searchUsers(_uiState.value.searchQuery, Firebase.auth.currentUser!!.uid)
            Log.d("SearchUsersViewModel", "getAndUpdateUsers: response: $response")
            updateState(
                _uiState.value.copy(
                    isLoading = false,
                    searchResults = response
                )
            )
        }
    }
}