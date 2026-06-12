package dev.jason.app.compose.vaultchat.feature_service.device

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val FeatureDeviceKoinModule = module {
    singleOf(::DeviceApiRepoImpl) { bind<DeviceApiRepository>() }
}