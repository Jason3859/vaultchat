package dev.jason.app.compose.messenger_app.local_storage.domain

import kotlinx.coroutines.flow.Flow

interface LocalStorageRepository {

    fun getUser(): Flow<User?>
    suspend fun addUser(user: User)
    suspend fun deleteUser()
}