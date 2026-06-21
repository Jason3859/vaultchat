package dev.jason.app.compose.vaultchat.feature.blocklist

import android.util.Log
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.User

class BlocklistApiService(private val blocklistApiRepository: BlocklistApiRepository) {

    suspend fun getBlocklist(): List<User> {
        return try {
            blocklistApiRepository.getBlocklist()
        } catch (e: Exception) {
            Log.e("BlocklistService", "getBlocklist: exception occurred", e)
            ToastController.showErrorOccurredToast()
            emptyList()
        }
    }

    suspend fun blockUser(user: User) {
        try {
            blocklistApiRepository.blockUser(user)
        } catch (e: Exception) {
            Log.e("BlocklistService", "blockUser: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }

    suspend fun unblockUser(user: User) {
        try {
            blocklistApiRepository.unblockUser(user)
        } catch (e: Exception) {
            Log.e("BlocklistService", "blockUser: exception occurred", e)
            ToastController.showErrorOccurredToast()
        }
    }
}