package dev.jason.app.compose.vaultchat.feature_service.logout

import dev.jason.app.compose.vaultchat.core.model.Device

interface LogoutRemoteRepository {

    suspend fun beginLogout(device: Device, clearMessages: Boolean, onLogoutSuccessful: () -> Unit): Int
}