package dev.jason.app.compose.vaultchat.messaging.data

import dev.jason.app.compose.vaultchat.messaging.data.remote.RemoteApi
import dev.jason.app.compose.vaultchat.messaging.data.remote.KtorRemoteApi
import dev.jason.app.compose.vaultchat.messaging.data.repository.LocalStorageRepoImpl
import dev.jason.app.compose.vaultchat.messaging.data.repository.RemoteApiRepoImpl
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingDataKoinModule = module {
    single<RemoteApi> { KtorRemoteApi(get<HttpClient>()) }

    singleOf(::RemoteApiRepoImpl) { bind<RemoteApiRepository>() }
    singleOf(::LocalStorageRepoImpl) { bind<LocalStorageRepository>() }
}