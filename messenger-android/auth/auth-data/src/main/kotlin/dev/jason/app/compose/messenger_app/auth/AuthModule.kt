package dev.jason.app.compose.messenger_app.auth

import dev.jason.app.compose.messenger_app.domain.AuthRepository
import dev.jason.app.compose.messenger_app.domain.LocalStorage
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDataModule = module {
    singleOf(::AuthRepoImpl) { bind<AuthRepository>() }
    singleOf(::LocalStorageImpl) { bind<LocalStorage>() }
    singleOf(::Authentication)
}