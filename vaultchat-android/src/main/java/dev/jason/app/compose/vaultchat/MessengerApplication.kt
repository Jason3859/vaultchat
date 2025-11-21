package dev.jason.app.compose.vaultchat

import android.app.Application
import dev.jason.app.compose.vaultchat.auth.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class MessengerApplication : Application() {

    private val baseModule = module {
        viewModelOf(::BaseViewModel)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MessengerApplication)
            modules(baseModule, authModule)
        }
    }
}