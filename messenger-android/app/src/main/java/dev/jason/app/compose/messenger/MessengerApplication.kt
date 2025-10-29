package dev.jason.app.compose.messenger

import android.app.Application
import dev.jason.app.compose.messenger_app.auth.authDataModule
import dev.jason.app.compose.messenger_app.auth_ui.authUiModule
import dev.jason.app.compose.messenger_app.local_storage.data.localStorageDataModule
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class MessengerApplication : Application() {

    private val applicationModule = module {
        single {
            OkHttpClient.Builder()
                .readTimeout(2.minutes)
                .webSocketCloseTimeout(2.minutes)
                .writeTimeout(2.minutes)
                .callTimeout(2.minutes)
                .connectTimeout(2.minutes)
                .pingInterval(30.seconds)
                .build()
        }

        single {
            BuildConfig.API_HOST_URL
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MessengerApplication)
            modules(applicationModule, authDataModule, authUiModule, localStorageDataModule)
        }
    }
}