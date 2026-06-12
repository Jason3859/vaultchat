package dev.jason.app.compose.vaultchat.feature_service.messages

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagesKoinModule = module {
    singleOf(::MessageDatabaseService)
    singleOf(::MessageDatabaseRepoImpl) { bind<MessageDatabaseRepository>() }

    singleOf(::MessageDatabaseService)
    singleOf(::MessageDatabaseRepoImpl) { bind<MessageDatabaseRepository>() }

    single<MessageDao> {
        MessageDatabase.getInstance(androidContext()).dao
    }
}