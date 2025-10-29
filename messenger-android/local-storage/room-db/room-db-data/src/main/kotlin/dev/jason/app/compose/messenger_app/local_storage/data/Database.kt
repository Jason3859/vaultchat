package dev.jason.app.compose.messenger_app.local_storage.data

import androidx.room.Database

object Database {

    @Database([Entities.UserEntity::class], version = 1)
    abstract class UserDatabase : androidx.room.RoomDatabase() {
        abstract fun userDao(): Dao.User
    }
}