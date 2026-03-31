package dev.jason.app.compose.vaultchat

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dev.jason.app.compose.vaultchat.core.local_storage.LocalStorageKoinModule
import dev.jason.app.compose.vaultchat.core.messaging.MessagingKoinModule
import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.data.RemoteApiImpl
import dev.jason.app.compose.vaultchat.auth.data.RetrofitApi
import dev.jason.app.compose.vaultchat.auth.ui.MainViewModel
import dev.jason.app.compose.vaultchat.core.R
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

class MainApplication : Application() {

    private val baseModule = module {
        viewModelOf(::MainViewModel)
        singleOf(::RemoteApiImpl) { bind<RemoteApi>() }

        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl("http://127.0.0.1:8080/")
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        single<RetrofitApi> {
            get<Retrofit>().create()
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startKoin {
            androidContext(this@MainApplication)
            modules(baseModule, MessagingKoinModule, LocalStorageKoinModule)
        }
    }

    private fun createNotificationChannel() {
        val id = getString(R.string.notification_channel_id)
        val name = getString(R.string.notification_channel_name)
        val description = getString(R.string.notification_channel_description)

        val channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
        channel.description = description

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}