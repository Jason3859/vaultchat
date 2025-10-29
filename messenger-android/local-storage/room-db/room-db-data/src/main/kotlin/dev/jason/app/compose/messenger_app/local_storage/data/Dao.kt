package dev.jason.app.compose.messenger_app.local_storage.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

object Dao {

    @Dao
    interface User {

        @Query("SELECT * FROM user")
        fun getUsers(): Flow<Entities.UserEntity?>

        @Insert
        suspend fun addUser(user: Entities.UserEntity)

        @Delete
        suspend fun deleteUser(user: Entities.UserEntity)
    }
}