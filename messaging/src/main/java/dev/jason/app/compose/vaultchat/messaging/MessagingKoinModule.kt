package dev.jason.app.compose.vaultchat.messaging

import dev.jason.app.compose.vaultchat.messaging.data.remote.FcmApi
import dev.jason.app.compose.vaultchat.messaging.data.repository.LocalStorageRepoImpl
import dev.jason.app.compose.vaultchat.messaging.data.repository.RemoteApiRepoImpl
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import dev.jason.app.compose.vaultchat.messaging.ui.main.MainHomeViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.messaging.MessagingViewModel
import dev.jason.app.compose.vaultchat.messaging.ui.search.SearchUsersViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val MessagingKoinModule = module {
    singleOf(::RemoteApiRepoImpl) { bind<RemoteApiRepository>() }
    singleOf(::LocalStorageRepoImpl) { bind<LocalStorageRepository>() }
    viewModelOf(::SearchUsersViewModel)
    viewModelOf(::MessagingViewModel)
    viewModelOf(::MainHomeViewModel)

    single<FcmApi> {
        get<Retrofit>().create()
    }
}