package dev.jason.app.compose.vaultchat.messaging.data

import dev.jason.app.compose.vaultchat.messaging.data.remote.FcmApi
import dev.jason.app.compose.vaultchat.messaging.data.repository.LocalStorageRepoImpl
import dev.jason.app.compose.vaultchat.messaging.data.repository.RemoteApiRepoImpl
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val MessagingDataKoinModule = module {
    single<FcmApi> { get<Retrofit>().create() }

    singleOf(::RemoteApiRepoImpl) { bind<RemoteApiRepository>() }
    singleOf(::LocalStorageRepoImpl) { bind<LocalStorageRepository>() }
}