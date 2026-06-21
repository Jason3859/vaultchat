package dev.jason.app.compose.vaultchat.ui.main.abstractt.profile

import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.DeviceUi

sealed interface ProfileUiAction {

    data object LogoutCurrentDeviceByClearingMessages : ProfileUiAction
    data object LogoutCurrentDeviceWithoutClearingMessages : ProfileUiAction

    data class LogoutDeviceByClearingMessages(val device: DeviceUi) : ProfileUiAction
    data class LogoutDeviceWithoutClearingMessages(val device: DeviceUi) : ProfileUiAction
}