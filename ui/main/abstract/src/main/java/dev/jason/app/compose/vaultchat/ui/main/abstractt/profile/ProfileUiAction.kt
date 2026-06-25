package dev.jason.app.compose.vaultchat.ui.main.abstractt.profile

import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.DeviceUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi

sealed interface ProfileUiAction {

    data object LogoutCurrentDeviceByClearingMessages : ProfileUiAction
    data object LogoutCurrentDeviceWithoutClearingMessages : ProfileUiAction

    data class LogoutDeviceByClearingMessages(val device: DeviceUi) : ProfileUiAction
    data class LogoutDeviceWithoutClearingMessages(val device: DeviceUi) : ProfileUiAction

    data class BlockUser(val user: UserUi, val onFinish: () -> Unit) : ProfileUiAction
    data class UnblockUser(val user: UserUi, val onFinish: () -> Unit) : ProfileUiAction
}