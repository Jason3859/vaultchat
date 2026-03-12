package dev.jason.app.compose.core.messaging

import dev.jason.app.compose.core.messaging.data.FcmApi
import dev.jason.app.compose.core.messaging.data.RemoteApiImpl
import dev.jason.app.compose.core.messaging.domain.RemoteApi
import dev.jason.app.compose.core.messaging.ui.AppViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val MessagingKoinModule = module {
    singleOf(::RemoteApiImpl) { bind<RemoteApi>() }
    viewModelOf(::AppViewModel)

    single<FcmApi> {
        get<Retrofit>().create()
    }
}