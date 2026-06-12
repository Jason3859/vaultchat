package dev.jason.app.compose.vaultchat.messaging

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.messaging
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.messaging.domain.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.ui.home.HomeScreen
import dev.jason.app.compose.vaultchat.messaging.ui.messaging.MessagingScreen
import dev.jason.app.compose.vaultchat.messaging.ui.nav.Route
import dev.jason.app.compose.vaultchat.messaging.ui.profile.ProfileScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.seconds


class MessagingActivity : ComponentActivity() {

    private val remoteApi: RemoteApi by inject()
    private var navEventIntent: Intent? = null

    private val firebaseUser = Firebase.auth.currentUser!!
    private val currentUser = User(
        firebaseUser.uid,
        firebaseUser.displayName!!,
        firebaseUser.photoUrl.toString().removeSuffix("=s96-c"),
        emptyList(), // TODO: fetch from server
        User.Status.Online
    )

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            MessagingState.deviceOnline()
        }

        override fun onLost(network: Network) {
            MessagingState.deviceOffline()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        MessagingState.apply {
            if (isNetworkAvailable(connectivityManager))
                deviceOnline()
            else
                deviceOffline()
        }

        lifecycleScope.launch {
            val token = Firebase.messaging.token.await()
            MessagingState.updateCurrentUser(currentUser)
            MessagingState.updateCurrentDevice(
                Device.getCurrentDevice(
                    this@MessagingActivity,
                    token
                )
            )
        }

        lifecycleScope.launch(Dispatchers.IO) {
            Firebase.auth.currentUser?.let { firebaseUser ->
                val isOnline = isNetworkAvailable(connectivityManager)
                while (true) {
                    if (isOnline) {
                        remoteApi.heartbeat(firebaseUser.uid)
                    }
                    delay(15.seconds)
                }
            }
        }

        handleIntent(intent)

        setContent {
            VaultChatTheme {
                App()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val destination = intent.getStringExtra("nav_destination")
        if (destination != null) {
            navEventIntent = intent
        }
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Composable
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    private fun App() {
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(true) {
            SnackbarController.flow.collect { message ->
                snackbarHostState.showSnackbar(message)
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { // base Scaffold for showing snack bars.
            val backStack = rememberNavBackStack(Route.Home)

            LaunchedEffect(true) {
                navEventIntent?.let { intent ->
                    val destination = intent.getStringExtra("nav_destination")
                    val id = intent.getStringExtra("id")
                    if (destination == "messaging" && id != null) {
                        backStack.add(Route.Messaging(id))
                        navEventIntent = null
                    }
                }
            }

            val navigateToProfileScreen: (showCurrentUserProfile: Boolean) -> Unit = { bool ->
                backStack.apply {
                    if (last() is Route.Profile) {
                        removeLastOrNull()
                    }

                    add(Route.Profile(bool))
                }
            }

            NavDisplay(
                backStack = backStack,
                sceneStrategies = listOf(rememberListDetailSceneStrategy()),
                entryProvider = entryProvider {
                    entry<Route.Home>(
                        metadata = ListDetailSceneStrategy.listPane()
                    ) {
                        HomeScreen(
                            onUserClick = {
                                backStack.apply {
                                    if (last() is Route.Messaging) {
                                        removeLastOrNull()
                                    }

                                    add(Route.Messaging(it.uid))
                                }
                            },
                            onProfileClick = { navigateToProfileScreen.invoke(true) }
                        )
                    }

                    entry<Route.Messaging>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) {
                        MessagingScreen(
                            otherUserUid = it.uid,
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            onUserProfileClick = { navigateToProfileScreen.invoke(false) }
                        )
                    }

                    entry<Route.Profile>(
                        metadata = ListDetailSceneStrategy.extraPane()
                    ) { route ->
                        ProfileScreen(
                            showCurrentUserProfile = route.showCurrentUserProfile,
                            onBack = { backStack.removeLastOrNull() },
                            onLogoutClick = {},
                            onDeviceLogoutClick = { _, _ -> }
                        )
                    }
                }
            )
        }
    }
}