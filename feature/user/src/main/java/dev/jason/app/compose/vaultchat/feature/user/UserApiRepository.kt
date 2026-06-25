package dev.jason.app.compose.vaultchat.feature.user

import dev.jason.app.compose.vaultchat.core.model.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.User

interface UserApiRepository {

    suspend fun heartbeat(uid: String)
    suspend fun registerUser(body: RegisterUserDto)
    suspend fun searchUser(displayName: String): List<User>
}