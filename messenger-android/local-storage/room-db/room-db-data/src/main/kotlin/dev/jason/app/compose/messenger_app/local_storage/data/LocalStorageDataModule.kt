package dev.jason.app.compose.messenger_app.local_storage.data

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.jason.app.compose.messenger_app.local_storage.domain.LocalStorageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

val localStorageDataModule = module {
    single {
        database<Database.UserDatabase>().userDao()
    }

    singleOf(::LocalStorageRepoImpl) { bind<LocalStorageRepository>() }
}

private inline fun <reified T : RoomDatabase> Scope.database(): T {

    return Room.databaseBuilder<T>(
        androidContext(),
        "${T::class.simpleName}.db"
    )
        .fallbackToDestructiveMigration(false)
        .build()
}