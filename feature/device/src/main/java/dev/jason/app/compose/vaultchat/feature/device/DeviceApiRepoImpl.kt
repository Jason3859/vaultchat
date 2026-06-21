package dev.jason.app.compose.vaultchat.feature.device

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.DeviceDto
import dev.jason.app.compose.vaultchat.core.model.toDevice
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class DeviceApiRepoImpl(
    private val client: HttpClient
) : DeviceApiRepository {

    override suspend fun getDevices(): List<Device> {
        return try {
            val response = client.get("$BASE_URL/device/mine") {
                parameter("uid", AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null"))
            }

            response.body<List<DeviceDto>>().map(DeviceDto::toDevice)
        } catch (e: Exception) {
            Log.e("RemoteDeviceApiRepoImpl", "getDevices: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }

    override suspend fun addDevice(device: Device) {
        client.post("$BASE_URL/device/add") {
            contentType(ContentType.Application.Json)
            setBody(device.toDto())
        }
    }
}