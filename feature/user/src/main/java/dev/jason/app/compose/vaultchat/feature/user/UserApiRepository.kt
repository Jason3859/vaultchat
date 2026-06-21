package dev.jason.app.compose.vaultchat.feature.user

import dev.jason.app.compose.vaultchat.core.model.User

interface UserApiRepository {

    typealias StatusCode = Int

    suspend fun heartbeat(uid: String)
    suspend fun registerUser(user: User): StatusCode
    suspend fun searchUser(displayName: String): List<User>
}