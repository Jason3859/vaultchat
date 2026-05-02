package dev.jason.app.compose.vaultchat.auth

import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.data.KtorRemoteApi
import io.ktor.client.HttpClient
import org.koin.dsl.module

val AuthKoinModule = module {
    single<RemoteApi> {
        KtorRemoteApi(get<HttpClient>())
    }
}