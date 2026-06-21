package dev.jason.app.compose.vaultchat.feature.device

import dev.jason.app.compose.vaultchat.core.model.Device

interface DeviceApiRepository {

    suspend fun getDevices(): List<Device>
    suspend fun addDevice(device: Device)
}