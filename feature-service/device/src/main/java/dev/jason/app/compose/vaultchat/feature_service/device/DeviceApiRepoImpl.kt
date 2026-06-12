package dev.jason.app.compose.vaultchat.feature_service.device

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.Device
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

class DeviceApiRepoImpl(
    private val baseUrl: String,
    private val client: HttpClient
) : DeviceApiRepository {

    override suspend fun getDevices(): List<Device> {
        return try {
            val response = client.get("$baseUrl/device/mine") {
                parameters {
                    append("uid", AppState.currentUser.value?.uid ?: throw IllegalStateException("user is null"))
                }
            }

            response.body()
        } catch (e: Exception) {
            Log.e("RemoteDeviceApiRepoImpl", "getDevices: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }
}