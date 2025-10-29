package dev.jason.app.compose.messenger_app.local_storage.data

import dev.jason.app.compose.messenger_app.local_storage.data.Entities.toDomain
import dev.jason.app.compose.messenger_app.local_storage.domain.LocalStorageRepository
import dev.jason.app.compose.messenger_app.local_storage.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalStorageRepoImpl(
    private val userDao: Dao.User,
) : LocalStorageRepository {
    override fun getUser(): Flow<User?> = userDao.getUsers().map { it?.toDomain() }

    override suspend fun addUser(user: User) {
        userDao.addUser(
            Entities.UserEntity(
                username = user.username, password = user.password
            )
        )
    }

    override suspend fun deleteUser() {
        val user = getUser().first()!!
        userDao.deleteUser(
            Entities.UserEntity(
                username = user.username, password = user.password
            )
        )
    }
}