package dev.jason.app.compose.vaultchat.messaging.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val repository: RemoteApiRepository,
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    private val currentUser by lazy { Firebase.auth.currentUser!! }

    private val _devices = MutableStateFlow(emptyList<Device>())
    val devices = _devices.asStateFlow()
        .onStart { fetchDevices() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _blocklist = MutableStateFlow(emptyList<User>())
    val blocklist = _blocklist.asStateFlow()
        .onStart { fetchBlocklist() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun fetchBlocklist() {
        viewModelScope.launch {
            repository.fetchBlocklist(currentUser.uid).let {
                _blocklist.update { it }
            }
        }
    }

    fun fetchDevices() {
        viewModelScope.launch {
            repository.fetchDevices(currentUser.uid).let { devices ->
                _devices.update { devices }
            }
        }
    }

    fun unblockUser(user: User) {
        viewModelScope.launch {
            repository.unblock(currentUser.uid, user.uid)
        }
    }

    fun logout() {
        // TODO: implement this
    }

    fun blockUser(user: User) {
        viewModelScope.launch {
            repository.block(currentUser.uid, user.uid)
        }
    }

    fun deleteMessageHistory(otherUser: User) {
        viewModelScope.launch {
            localStorageRepository.deleteMessageHistory(currentUser.uid, otherUser.uid)
        }
    }
}