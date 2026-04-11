package dev.jason.app.compose.vaultchat.messaging.domain

import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MessagingState {

    private val _otherUserUid = MutableStateFlow<String?>(null)
    val otherUserUid = _otherUserUid.asStateFlow()

    private val _connectionsStatus = MutableStateFlow<List<User>>(emptyList())
    val connectionsStatus = _connectionsStatus.asStateFlow()

    private val _currentDevice = MutableStateFlow<Device?>(null)
    val currentDevice = _currentDevice.asStateFlow()

    fun updateOtherUserId(string: String?) {
        _otherUserUid.update { string }
    }

    fun updateCurrentDevice(device: Device) {
        _currentDevice.update { device }
    }

    fun updateConnections(list: List<User>) {
        _connectionsStatus.update { list }
    }

    fun updateConnectionsStatus(uid: String, status: User.Status) {
        _connectionsStatus.update { list ->
            list.map { user ->
                if (user.uid == uid) {
                    user.copy(status = status)
                } else {
                    user
                }
            }
        }
    }
}