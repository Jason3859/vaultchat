package dev.jason.app.compose.vaultchat.ui.concrete.auth

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val AuthKoinModule = module {
    viewModelOf(::AuthViewModel)
    singleOf(::KtorRemoteApiRepository) { bind<RemoteApiRepository>() }
}