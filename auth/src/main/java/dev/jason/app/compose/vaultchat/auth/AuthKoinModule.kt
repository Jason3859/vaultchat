package dev.jason.app.compose.vaultchat.auth

import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val AuthKoinModule = module {
    single<RemoteApi> {
        get<Retrofit>().create()
    }
}