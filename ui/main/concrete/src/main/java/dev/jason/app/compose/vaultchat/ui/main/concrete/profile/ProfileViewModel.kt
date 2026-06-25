package dev.jason.app.compose.vaultchat.ui.main.concrete.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.feature.blocklist.BlocklistApiService
import dev.jason.app.compose.vaultchat.feature.device.DeviceApiService
import dev.jason.app.compose.vaultchat.feature.logout.LogoutService
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.DeviceUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.toDevice
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.toUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.toUser
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
    private val blocklistApiService: BlocklistApiService
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
}