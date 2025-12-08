package dev.jason.app.compose.vaultchat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.auth.ui.AuthNavGraph
import dev.jason.app.compose.vaultchat.core.nav.AppRoute
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme
import dev.jason.app.compose.vaultchat.core.util.DeviceType
import dev.jason.app.compose.vaultchat.web_socket.ui.screen.ChatScreen

class MessengerActivity : ComponentActivity() {

    private companion object {
        const val AUTH_STATUS_SHARED_PREFS_NAME = "auth_status"
        const val IS_SIGNED_IN_PREF_NAME = "is_signed_in"
    }

    private val sharedPrefs by lazy { getSharedPreferences(AUTH_STATUS_SHARED_PREFS_NAME, MODE_PRIVATE) }

    private val startDestination by lazy {
        if (sharedPrefs.getBoolean(IS_SIGNED_IN_PREF_NAME, false))
            AppRoute.WebSocketConnection
        else
            AppRoute.Authentication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MessengerActivity", "onCreate: $startDestination")

        setContent {
            MessengerTheme {
                val backStack = rememberNavBackStack(startDestination)

                val deviceType = DeviceType.rememberWindowSize()

                LaunchedEffect(deviceType) {
                    Log.d("MessengerActivity", "onCreate: current device type: $deviceType")
                }

                NavDisplay(
                    backStack = backStack,
                    entryProvider = entryProvider {
                        entry<AppRoute.Authentication> {
                            AuthNavGraph(deviceType) {
                                sharedPrefs.edit().putBoolean(IS_SIGNED_IN_PREF_NAME, true).apply()
                                backStack.add(AppRoute.WebSocketConnection)
                                backStack.removeAt(0)
                            }
                        }

                        entry<AppRoute.WebSocketConnection> {
                            ChatScreen()
                        }
                    }
                )
            }
        }
    }
}