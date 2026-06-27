package dev.jason.app.compose.vaultchat.feature.device

import dev.jason.app.compose.vaultchat.core.model.device.Device

interface DeviceApiRepository {

    suspend fun getDevices(): List<Device>
}