package dev.jason.app.compose.vaultchat.feature_service.user

import dev.jason.app.compose.vaultchat.core.model.User

interface UserApiRepository {

    suspend fun registerUser(user: User)
    suspend fun searchUser(displayName: String): List<User>
}