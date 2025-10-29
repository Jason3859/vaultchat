package dev.jason.app.compose.messenger_app.local_storage.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.jason.app.compose.messenger_app.local_storage.domain.User

object Entities {

    @Entity(tableName = "user")
    data class UserEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val username: String,
        val password: String
    )

    fun UserEntity.toDomain() = User(username, password)
}