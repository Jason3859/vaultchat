package dev.jason.app.compose.messenger_app.domain

import kotlinx.coroutines.flow.Flow

interface LocalStorage {

    fun getUser(): Flow<User?>
    suspend fun addUser(username: String, password: String)
}