package dev.jason.app.compose.vaultchat.messaging.data

import dev.jason.app.compose.vaultchat.messaging.data.remote.KtorRemoteApi
import dev.jason.app.compose.vaultchat.messaging.data.remote.RemoteApi
import dev.jason.app.compose.vaultchat.messaging.data.repository.LocalStorageRepoImpl
import dev.jason.app.compose.vaultchat.messaging.data.repository.RemoteApiRepoImpl
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingDataKoinModule = module {
    singleOf(::KtorRemoteApi) { bind<RemoteApi>() }
    singleOf(::RemoteApiRepoImpl) { bind<RemoteApiRepository>() }
    singleOf(::LocalStorageRepoImpl) { bind<LocalStorageRepository>() }
}