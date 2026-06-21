package dev.jason.app.compose.vaultchat.feature.logout

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val FeatureLogoutKoinModule = module {
    singleOf(::LogoutService)
    singleOf(::LogoutRemoteRepoImpl) { bind<LogoutRemoteRepository>() }
}