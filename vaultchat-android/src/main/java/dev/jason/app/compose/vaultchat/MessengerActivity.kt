package dev.jason.app.compose.vaultchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.auth.ui.AuthNavGraph
import dev.jason.app.compose.vaultchat.theme.MessengerTheme
import dev.jason.app.compose.vaultchat.util.DeviceType
import org.koin.androidx.compose.koinViewModel

class MessengerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MessengerTheme {
                val baseViewModel: BaseViewModel = koinViewModel()
                val startDestination by baseViewModel.startDestination.collectAsState()

                val backStack = rememberNavBackStack(startDestination)

                val deviceType = DeviceType.rememberWindowSize()

                NavDisplay(backStack) { key ->
                    when (key) {
                        is AppRoute.Authentication -> {
                            NavEntry(key) {
                                AuthNavGraph(deviceType) {
                                    backStack.add(AppRoute.WebSocketConnection)
                                    backStack.removeAt(0)
                                }
                            }
                        }

                        is AppRoute.WebSocketConnection -> {
                            NavEntry(key) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("TODO")
                                }
                            }
                        }

                        else -> throw IllegalStateException()
                    }
                }
            }
        }
    }
}