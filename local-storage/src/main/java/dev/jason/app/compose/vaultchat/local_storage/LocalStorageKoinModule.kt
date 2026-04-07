package dev.jason.app.compose.vaultchat.local_storage

import dev.jason.app.compose.vaultchat.local_storage.data.MessageDao
import dev.jason.app.compose.vaultchat.local_storage.data.MessageDatabase
import dev.jason.app.compose.vaultchat.local_storage.data.MessageRepoImpl
import dev.jason.app.compose.vaultchat.local_storage.domain.MessageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val LocalStorageKoinModule = module {
    singleOf(::MessageRepoImpl) { bind<MessageRepository>() }
    single<MessageDao> {
        MessageDatabase.getDatabase(androidContext()).messageDao()
    }
}