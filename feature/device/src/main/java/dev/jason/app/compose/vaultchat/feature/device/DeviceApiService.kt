package dev.jason.app.compose.vaultchat.feature.device

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.Device

class DeviceApiService(private val repository: DeviceApiRepository) {

    suspend fun getDevices(): List<Device> {
        return try {
            repository.getDevices()
        } catch (e: Exception) {
            Log.e("DeviceApiService", "getDevices: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }

    suspend fun addDevice(device: Device) {
        try {
            repository.addDevice(device)
        } catch (e: Exception) {
            Log.e("DeviceApiService", "addDevice: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }
}