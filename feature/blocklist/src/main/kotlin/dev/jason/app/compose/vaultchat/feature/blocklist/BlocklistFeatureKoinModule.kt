package dev.jason.app.compose.vaultchat.feature.blocklist

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val BlocklistFeatureKoinModule = module {
    singleOf(::BlocklistApiService)
    singleOf(::BlocklistApiRepoImpl) { bind<BlocklistApiRepository>() }
}