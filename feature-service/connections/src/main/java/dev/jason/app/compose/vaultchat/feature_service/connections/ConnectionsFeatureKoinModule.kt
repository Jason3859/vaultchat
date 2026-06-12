package dev.jason.app.compose.vaultchat.feature_service.connections

import dev.jason.app.compose.vaultchat.feature_service.connections.api.ConnectionsApiRepoImpl
import dev.jason.app.compose.vaultchat.feature_service.connections.api.ConnectionsApiRepository
import dev.jason.app.compose.vaultchat.feature_service.connections.db.ConnectionsDao
import dev.jason.app.compose.vaultchat.feature_service.connections.db.ConnectionsDatabase
import dev.jason.app.compose.vaultchat.feature_service.connections.db.ConnectionsDbRepoImpl
import dev.jason.app.compose.vaultchat.feature_service.connections.db.ConnectionsDbRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ConnectionsFeatureKoinModule = module {
    singleOf(::ConnectionsService)
    singleOf(::ConnectionsDbRepoImpl) { bind<ConnectionsDbRepository>() }
    singleOf(::ConnectionsApiRepoImpl) { bind<ConnectionsApiRepository>() }

    single<ConnectionsDao> { ConnectionsDatabase.getDatabase(androidContext()).dao() }
}