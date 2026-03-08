package dev.jason.app.compose.messaging

import dev.jason.app.compose.messaging.data.FcmApi
import dev.jason.app.compose.messaging.data.RemoteApiImpl
import dev.jason.app.compose.messaging.domain.RemoteApi
import dev.jason.app.compose.messaging.ui.AppViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

val MessagingKoinModule = module {
    singleOf(::RemoteApiImpl) { bind<RemoteApi>() }
    viewModelOf(::AppViewModel)

    single<FcmApi> {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("http://10.0.2.2:8080/")
            .build()
            .create()
    }
}