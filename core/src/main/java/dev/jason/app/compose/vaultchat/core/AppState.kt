package dev.jason.app.compose.vaultchat.core

import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AppState {

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser = _otherUser.asStateFlow()

    private val _currentDevice = MutableStateFlow<Device?>(null)
    val currentDevice = _currentDevice.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()

    private val _isAppInForeground = MutableStateFlow(false)
    val isAppInForeground = _isAppInForeground.asStateFlow()

    fun updateOtherUser(user: User?) {
        _otherUser.update { user }
    }

    fun updateCurrentDevice(device: Device) {
        _currentDevice.update { device }
    }

    fun updateCurrentUser(user: User) {
        _currentUser.update { user }
    }

    fun deviceOnline() {
        _isOnline.update { true }
    }

    fun deviceOffline() {
        _isOnline.update { false }
    }

    fun clearOtherUser() {
        updateOtherUser(null)
    }

    fun appInForeground() {
        _isAppInForeground.update { true }
    }

    fun appInBackground() {
        _isAppInForeground.update { false }
    }
}