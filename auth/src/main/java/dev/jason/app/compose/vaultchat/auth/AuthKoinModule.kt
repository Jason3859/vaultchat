package dev.jason.app.compose.vaultchat.auth

import dev.jason.app.compose.vaultchat.auth.data.KtorRemoteApi
import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val AuthKoinModule = module {
    singleOf(::KtorRemoteApi) { bind<RemoteApi>() }
}