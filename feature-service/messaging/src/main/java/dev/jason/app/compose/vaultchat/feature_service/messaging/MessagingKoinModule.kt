package dev.jason.app.compose.vaultchat.feature_service.messaging

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingKoinModule = module {
    singleOf(::MessagingApiService)
    singleOf(::MessagingApiRepoImpl) { bind<MessagingApiRepository>() }
}