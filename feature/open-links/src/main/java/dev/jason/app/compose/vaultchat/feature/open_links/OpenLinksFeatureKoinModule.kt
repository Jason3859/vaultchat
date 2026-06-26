package dev.jason.app.compose.vaultchat.feature.open_links

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val OpenLinksFeatureKoinModule = module {

    singleOf(::OpenLinksService)
}