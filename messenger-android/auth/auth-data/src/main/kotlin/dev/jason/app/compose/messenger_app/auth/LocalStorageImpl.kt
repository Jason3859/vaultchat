package dev.jason.app.compose.messenger_app.auth

import dev.jason.app.compose.messenger_app.domain.LocalStorage
import dev.jason.app.compose.messenger_app.domain.User
import dev.jason.app.compose.messenger_app.local_storage.domain.LocalStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalStorageImpl(private val repository: LocalStorageRepository) : LocalStorage {

    override fun getUser(): Flow<User?> {
        return repository.getUser().map { userEntity ->
            userEntity?.let {
                User(
                    userEntity.username,
                    userEntity.password
                )
            }
        }
    }

    override suspend fun addUser(username: String, password: String) {
        val user = dev.jason.app.compose.messenger_app.local_storage.domain.User(username, password)
        repository.addUser(user)
    }
}