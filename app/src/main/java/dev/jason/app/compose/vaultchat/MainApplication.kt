package dev.jason.app.compose.vaultchat

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Bundle
import dev.jason.app.compose.vaultchat.auth.AuthKoinModule
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.local_storage.LocalStorageKoinModule
import dev.jason.app.compose.vaultchat.messaging.MessagingKoinModule
import dev.jason.app.compose.vaultchat.messaging.data.MessagingDataKoinModule
import dev.jason.app.compose.vaultchat.messaging.ui.MessagingUiKoinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {

    private var activityCount = 0

    private val baseModule = module {
        single<String> {
            // base url
            "http://10.0.2.2:8080"
        }

        single<HttpClient> {
            HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                engine {
                    connectTimeout = 100_000
                    socketTimeout = 100_000
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startKoin {
            androidContext(this@MainApplication)
            modules(baseModule, AuthKoinModule, LocalStorageKoinModule, MessagingKoinModule, MessagingDataKoinModule, MessagingUiKoinModule)
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {
                if (activityCount == 0) {
                    AppState.appInForeground()
                }
                activityCount++
            }
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                activityCount--
                if (activityCount == 0) {
                    AppState.appInBackground()
                }
            }
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
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

    private fun initializeAppStateFields() {
        // TODO: implement
    }
}