package dev.jason.app.compose.vaultchat

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.local_storage.LocalStorageKoinModule
import dev.jason.app.compose.vaultchat.messaging.MessagingKoinModule
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MainApplication : Application() {

    private val baseModule = module {
        single<Retrofit> {
            Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
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