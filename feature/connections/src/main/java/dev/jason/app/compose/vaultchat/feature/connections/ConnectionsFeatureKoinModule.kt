package dev.jason.app.compose.vaultchat.feature.connections

import dev.jason.app.compose.vaultchat.feature.connections.api.ConnectionsApiRepoImpl
import dev.jason.app.compose.vaultchat.feature.connections.api.ConnectionsApiRepository
import dev.jason.app.compose.vaultchat.feature.connections.db.ConnectionsDao
import dev.jason.app.compose.vaultchat.feature.connections.db.ConnectionsDatabase
import dev.jason.app.compose.vaultchat.feature.connections.db.ConnectionsDbRepoImpl
import dev.jason.app.compose.vaultchat.feature.connections.db.ConnectionsDbRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ConnectionsFeatureKoinModule = module {
    singleOf(::ConnectionsService) { createdAtStart() }
    singleOf(::ConnectionsDbRepoImpl) { bind<ConnectionsDbRepository>() }
    singleOf(::ConnectionsApiRepoImpl) { bind<ConnectionsApiRepository>() }

    single<ConnectionsDao> { ConnectionsDatabase.getDatabase(androidContext()).dao() }
}