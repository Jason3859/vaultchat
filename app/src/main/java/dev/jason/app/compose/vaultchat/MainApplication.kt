package dev.jason.app.compose.vaultchat

import android.app.Application
import dev.jason.app.compose.core.messaging.MessagingKoinModule
import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.data.RemoteApiImpl
import dev.jason.app.compose.vaultchat.auth.data.RetrofitApi
import dev.jason.app.compose.vaultchat.auth.ui.MainViewModel
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
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        single<RetrofitApi> {
            get<Retrofit>().create()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(baseModule, MessagingKoinModule)
        }
    }
}