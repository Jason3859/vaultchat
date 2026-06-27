package dev.jason.app.compose.vaultchat.ui.main.abstractt.profile

import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.user.UserUi

sealed interface ProfileUiAction {

    data object LogoutCurrentDeviceByClearingMessages : ProfileUiAction
    data object LogoutCurrentDeviceWithoutClearingMessages : ProfileUiAction

    data class LogoutDeviceByClearingMessages(val device: DeviceUi) : ProfileUiAction
    data class LogoutDeviceWithoutClearingMessages(val device: DeviceUi) : ProfileUiAction

    data class BlockUser(val user: UserUi, val onFinish: () -> Unit) : ProfileUiAction
    data class UnblockUser(val user: UserUi, val onFinish: () -> Unit) : ProfileUiAction

    data object DeleteMessagesHistory : ProfileUiAction
}