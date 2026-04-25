package dev.jason.app.compose.vaultchat.messaging

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

val MessagingKoinModule = module {
    single<RemoteApi> { get<Retrofit>().create() }
}