package dev.jason.app.compose.vaultchat.feature.messages

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val MessagesFeatureKoinModule = module {
    singleOf(::MessageDatabaseService) { createdAtStart() }
    singleOf(::MessageDatabaseRepoImpl) { bind<MessageDatabaseRepository>() }

    single<MessageDao> {
        MessageDatabase.getInstance(androidContext()).dao
    }
}