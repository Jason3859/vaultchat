package dev.jason.app.compose.vaultchat.feature_service.user

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val UserServiceKoinModule = module {
    singleOf(::UserApiService)
    singleOf(::UserApiRepoImpl) { bind<UserApiRepository>() }
}