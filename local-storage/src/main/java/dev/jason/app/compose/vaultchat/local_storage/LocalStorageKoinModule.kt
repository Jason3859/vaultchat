package dev.jason.app.compose.vaultchat.local_storage

import dev.jason.app.compose.vaultchat.local_storage.connections.ConnectionsDao
import dev.jason.app.compose.vaultchat.local_storage.connections.ConnectionsDatabase
import dev.jason.app.compose.vaultchat.local_storage.messages.MessageDao
import dev.jason.app.compose.vaultchat.local_storage.messages.MessageDatabase
import dev.jason.app.compose.vaultchat.local_storage.repo.connections.ConnectionsRepoImpl
import dev.jason.app.compose.vaultchat.local_storage.repo.connections.ConnectionsRepository
import dev.jason.app.compose.vaultchat.local_storage.repo.messages.MessageRepoImpl
import dev.jason.app.compose.vaultchat.local_storage.repo.messages.MessageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val LocalStorageKoinModule = module {
    singleOf(::MessageRepoImpl) { bind<MessageRepository>() }
    single<MessageDao> {
        MessageDatabase.getDatabase(androidContext()).messageDao()
    }

    singleOf(::ConnectionsRepoImpl) { bind<ConnectionsRepository>() }
    single<ConnectionsDao> {
        ConnectionsDatabase.getDatabase(androidContext()).dao()
    }
}