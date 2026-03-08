package dev.jason.app.compose.vaultchat

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.jason.app.compose.messaging.ExampleMessagingComposable
import dev.jason.app.compose.vaultchat.auth.ui.ExampleSignInScreen
import dev.jason.app.compose.vaultchat.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    companion object {
        const val AUTH_STATUS_SHARED_PREFS_NAME = "auth_status"
        const val IS_SIGNED_IN_PREF_NAME = "is_signed_in"
    }

    val sharedPrefs: SharedPreferences by lazy { getSharedPreferences(AUTH_STATUS_SHARED_PREFS_NAME, MODE_PRIVATE) }

    private val startDestination by lazy {
        if (sharedPrefs.getBoolean(IS_SIGNED_IN_PREF_NAME, false))
            MainViewModel.Routes.Messaging
        else
            MainViewModel.Routes.Auth
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            VaultChatTheme {
                val mainViewModel: MainViewModel = koinViewModel()
                val currentRoute by mainViewModel.currentRoute.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(true) {
                    mainViewModel.onAction(MainViewModel.Action.UpdateRoute(startDestination))
                }

                LaunchedEffect(true) {
                    SnackbarController.events.collect { event ->
                        event?.let {
                            snackbarHostState.showSnackbar(it)
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) {
                    when (currentRoute) {
                        is MainViewModel.Routes.Auth -> ExampleSignInScreen()
                        is MainViewModel.Routes.Messaging -> ExampleMessagingComposable()

                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(150.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
            }
        }
    }
}