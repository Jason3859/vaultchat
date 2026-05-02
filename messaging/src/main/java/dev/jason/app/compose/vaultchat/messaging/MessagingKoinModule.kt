package dev.jason.app.compose.vaultchat.messaging

import io.ktor.client.HttpClient
import org.koin.dsl.module

val MessagingKoinModule = module {
    single<RemoteApi> { KtorRemoteApi(get<HttpClient>()) }
}