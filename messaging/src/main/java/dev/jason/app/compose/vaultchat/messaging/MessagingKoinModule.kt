package dev.jason.app.compose.vaultchat.messaging

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingKoinModule = module {
    singleOf(::KtorRemoteApi) { bind<RemoteApi>() }
}