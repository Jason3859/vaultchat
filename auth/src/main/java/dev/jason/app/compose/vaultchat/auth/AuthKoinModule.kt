package dev.jason.app.compose.vaultchat.auth

import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.data.RemoteApiImpl
import dev.jason.app.compose.vaultchat.auth.data.RetrofitApi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val AuthKoinModule = module {

    singleOf(::RemoteApiImpl) { bind<RemoteApi>() }
    single<RetrofitApi> {
        get<Retrofit>().create()
    }
}