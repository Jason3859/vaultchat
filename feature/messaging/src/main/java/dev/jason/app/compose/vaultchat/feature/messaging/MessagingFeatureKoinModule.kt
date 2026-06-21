package dev.jason.app.compose.vaultchat.feature.messaging

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingFeatureKoinModule = module {
    singleOf(::MessagingApiService)
    singleOf(::MessagingApiRepoImpl) { bind<MessagingApiRepository>() }
}