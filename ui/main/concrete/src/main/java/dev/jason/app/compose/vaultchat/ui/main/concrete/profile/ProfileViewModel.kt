package dev.jason.app.compose.vaultchat.ui.main.concrete.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.device.toDevice
import dev.jason.app.compose.vaultchat.core.model.device.toUi
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.core.model.user.toUser
import dev.jason.app.compose.vaultchat.feature.blocklist.BlocklistApiService
import dev.jason.app.compose.vaultchat.feature.device.DeviceApiService
import dev.jason.app.compose.vaultchat.feature.logout.LogoutService
import dev.jason.app.compose.vaultchat.feature.messages.MessageDatabaseService
import dev.jason.app.compose.vaultchat.ui.main.abstractt.profile.ProfileUiAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutService: LogoutService,
    private val deviceApiService: DeviceApiService,
    private val blocklistApiService: BlocklistApiService,
    private val messageDatabaseService: MessageDatabaseService
) : ViewModel() {

    private val _devices = MutableStateFlow<List<DeviceUi>>(emptyList())
    val devices = _devices.asStateFlow()
        .onStart { fetchDevices() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _blocklist = MutableStateFlow(emptyList<UserUi>())
    val blocklist = _blocklist.asStateFlow()
        .onStart { fetchBlocklist() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAction(action: ProfileUiAction) {
        when (action) {
            ProfileUiAction.LogoutCurrentDeviceByClearingMessages -> logoutCurrentDeviceByClearingMessages()
            ProfileUiAction.LogoutCurrentDeviceWithoutClearingMessages -> logoutCurrentDeviceWithoutClearingMessages()

            is ProfileUiAction.LogoutDeviceByClearingMessages -> logoutDeviceByClearingMessages(action.device)
            is ProfileUiAction.LogoutDeviceWithoutClearingMessages -> logoutDeviceWithoutClearingMessages(action.device)

            is ProfileUiAction.BlockUser -> blockUser(action)
            is ProfileUiAction.UnblockUser -> unblockUser(action)

            is ProfileUiAction.DeleteMessagesHistory -> deleteMessagesHistory()
        }
    }

    private fun logoutDeviceWithoutClearingMessages(device: DeviceUi) {
        viewModelScope.launch {
            logoutService.beginLogout(
                device = device.toDevice(),
                clearMessages = false
            )
        }
    }

    private fun logoutDeviceByClearingMessages(device: DeviceUi) {
        viewModelScope.launch {
            logoutService.beginLogout(
                device = device.toDevice(),
                clearMessages = true
            )
        }
    }

    private fun logoutCurrentDeviceWithoutClearingMessages() {
        viewModelScope.launch {
            logoutService.logoutCurrentDevice(clearMessages = false)
        }
    }

    private fun logoutCurrentDeviceByClearingMessages() {
        viewModelScope.launch {
            logoutService.logoutCurrentDevice(clearMessages = true)
        }
    }

    private fun fetchDevices() {
        viewModelScope.launch {
            val devices = deviceApiService.getDevices()
            _devices.update { devices.map(Device::toUi) }
        }
    }

    private fun fetchBlocklist() {
        viewModelScope.launch {
            val blocklist = blocklistApiService.getBlocklist()
            _blocklist.update { blocklist.map(User::toUi) }
        }
    }

    private fun blockUser(action: ProfileUiAction.BlockUser) {
        viewModelScope.launch {
            blocklistApiService.blockUser(action.user.toUser())
            action.onFinish.invoke()
        }
    }

    private fun unblockUser(action: ProfileUiAction.UnblockUser) {
        viewModelScope.launch {
            blocklistApiService.unblockUser(action.user.toUser())
            action.onFinish.invoke()
        }
    }

    private fun deleteMessagesHistory() {
        viewModelScope.launch {
            val currentUser = AppState.currentUser.value!!
            val otherUser = AppState.otherUser.value!!

            messageDatabaseService.deleteMessageHistory(currentUser.uid, otherUser.uid)
        }
    }
}