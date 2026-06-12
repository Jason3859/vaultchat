package dev.jason.app.compose.vaultchat.feature_service.device

import dev.jason.app.compose.vaultchat.core.model.Device

interface DeviceApiRepository {

    suspend fun getDevices(): List<Device>
}