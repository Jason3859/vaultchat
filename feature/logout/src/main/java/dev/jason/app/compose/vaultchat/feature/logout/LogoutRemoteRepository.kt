package dev.jason.app.compose.vaultchat.feature.logout

import dev.jason.app.compose.vaultchat.core.model.device.Device

interface LogoutRemoteRepository {

    suspend fun beginLogout(device: Device, clearMessages: Boolean, onLogoutSuccessful: () -> Unit): Int
}