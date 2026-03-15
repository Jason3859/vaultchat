package dev.jason.app.compose.core.messaging

import dev.jason.app.compose.core.messaging.data.remote.FcmApi
import dev.jason.app.compose.core.messaging.data.remote.RemoteApiImpl
import dev.jason.app.compose.core.messaging.domain.remote.RemoteApi
import dev.jason.app.compose.core.messaging.ui.screen.main.MainHomeViewModel
import dev.jason.app.compose.core.messaging.ui.screen.messaging.MessagingViewModel
import dev.jason.app.compose.core.messaging.ui.screen.search.SearchUsersViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val MessagingKoinModule = module {
    singleOf(::RemoteApiImpl) { bind<RemoteApi>() }
    viewModelOf(::SearchUsersViewModel)
    viewModelOf(::MessagingViewModel)
    viewModelOf(::MainHomeViewModel)

    single<FcmApi> {
        get<Retrofit>().create()
    }
}