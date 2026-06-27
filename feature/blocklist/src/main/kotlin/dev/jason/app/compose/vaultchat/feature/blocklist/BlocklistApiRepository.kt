package dev.jason.app.compose.vaultchat.feature.blocklist

import dev.jason.app.compose.vaultchat.core.model.user.User

interface BlocklistApiRepository {

    suspend fun getBlocklist(): List<User>

    suspend fun blockUser(user: User)
    suspend fun unblockUser(user: User)
}