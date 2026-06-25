package dev.jason.app.compose.vaultchat.feature.blocklist

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.User

class BlocklistApiService(private val blocklistApiRepository: BlocklistApiRepository) {

    suspend fun getBlocklist(): List<User> {
        return try {
            blocklistApiRepository.getBlocklist()
        } catch (e: Exception) {
            Log.e("BlocklistService", "getBlocklist: exception occurred", e)
            emptyList()
        }
    }

    suspend fun blockUser(user: User) {
        try {
            blocklistApiRepository.blockUser(user)
            AppEvents.sendEvent(AppEvent.ReFetchConnections)
            Log.d("BlocklistApiService", "blockUser: sent app event")
        } catch (e: Exception) {
            Log.e("BlocklistService", "blockUser: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun unblockUser(user: User) {
        try {
            blocklistApiRepository.unblockUser(user)
            AppEvents.sendEvent(AppEvent.ReFetchConnections)
        } catch (e: Exception) {
            Log.e("BlocklistService", "unblockUser: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }
}