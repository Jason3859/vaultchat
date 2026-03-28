package dev.jason.app.compose.vaultchat.core.local_storage

import dev.jason.app.compose.vaultchat.core.local_storage.messages.data.MessageDao
import dev.jason.app.compose.vaultchat.core.local_storage.messages.data.MessageDatabase
import dev.jason.app.compose.vaultchat.core.local_storage.messages.data.MessageRepoImpl
import dev.jason.app.compose.vaultchat.core.local_storage.messages.domain.MessageRepository
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