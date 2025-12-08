package dev.jason.app.compose.vaultchat

import android.app.Application
import dev.jason.app.compose.vaultchat.auth.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MessengerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MessengerApplication)
            modules(authModule)
        }
    }
}