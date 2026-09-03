package dev.jason.app.compose.vaultchat.feature.messaging

import io.ktor.client.HttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagingFeatureKoinModule = module {
    singleOf(::MessagingApiService)
    singleOf(::MessagingApiRepoImpl) { bind<MessagingApiRepository>() }

    single {
        StompClient(KtorWebSocketClient(get<HttpClient>()))
    }
}