package dev.jason.app.compose.vaultchat

import android.app.Application
import dev.jason.app.compose.messaging.MessagingKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class MainApplication : Application() {

    private val module = module {
        viewModelOf(::MainViewModel)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(module, MessagingKoinModule)
        }
    }
}