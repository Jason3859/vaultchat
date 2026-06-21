package dev.jason.app.compose.vaultchat.feature.device

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DeviceFeatureKoinModule = module {
    singleOf(::DeviceApiService)
    singleOf(::DeviceApiRepoImpl) { bind<DeviceApiRepository>() }
}