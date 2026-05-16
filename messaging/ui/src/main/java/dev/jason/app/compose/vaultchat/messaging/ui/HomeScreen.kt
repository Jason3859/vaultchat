package dev.jason.app.compose.vaultchat.messaging.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.MessagingState
import dev.jason.app.compose.vaultchat.messaging.domain.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.ui.main.MainHomeScreen
import dev.jason.app.compose.vaultchat.messaging.ui.messaging.MessagingScreen
import dev.jason.app.compose.vaultchat.messaging.ui.nav.Route
import dev.jason.app.compose.vaultchat.messaging.ui.profile.ProfileScreen
import dev.jason.app.compose.vaultchat.messaging.ui.profile.UserInfoScreen
import dev.jason.app.compose.vaultchat.messaging.ui.search.SearchUsersScreen
import dev.jason.app.compose.vaultchat.messaging.ui.search.SearchUsersViewModel
import kotlinx.coroutines.flow.SharedFlow
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(isOffline: Boolean, navEvents: SharedFlow<Intent>? = null) {
    val mainBackStack = rememberNavBackStack(Route.Home)
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(true) {
        SnackbarController.flow.collect { string ->
            snackbarHostState.showSnackbar(string)
        }
    }

    // FIXME: not opening MessagingScreen when app is killed. instead, opening app only 
    LaunchedEffect(navEvents) {
        navEvents?.collect { intent ->
            val destination = intent.getStringExtra("nav_destination")
            val id = intent.getStringExtra("id")
            if (destination == "messaging" && id != null) {
                val name = intent.getStringExtra("display_name") ?: "User"
                val photo = intent.getStringExtra("profile_picture_url") ?: ""
                val status = User.Status.valueOf(intent.getStringExtra("status") ?: "Online")

                mainBackStack.add(Route.Messaging(id, name, photo, status))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavDisplay(
            backStack = mainBackStack,
            entryProvider = entryProvider {
                entry<Route.Home> {
                    HomeScreenCore(mainBackStack, isOffline)
                }

                entry<Route.Messaging> {
                    MessagingState.updateOtherUserId(it.uid)
                    MessagingScreen(
                        otherUser = User(
                            it.uid,
                            it.displayName,
                            it.profilePictureUrl,
                            emptyList(),
                            it.status
                        ),
                        onBackClick = {
                            MessagingState.updateOtherUserId(null)
                            mainBackStack.removeLastOrNull()
                        },
                        isOffline = isOffline,
                        onUserInfoClick = {
                            mainBackStack.add(
                                Route.UserInfo(
                                    it.uid,
                                    it.displayName,
                                    it.profilePictureUrl
                                )
                            )
                        }
                    )
                }

                entry<Route.UserInfo> {
                    UserInfoScreen(
                        user = User(
                            it.uid,
                            it.displayName,
                            it.profilePictureUrl,
                            emptyList(),
                            User.Status.Online
                        ),
                        onBackClick = mainBackStack::removeLastOrNull,
                        navigateBackToHomeScreen = {
                            mainBackStack.removeLastOrNull()
                            mainBackStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun HomeScreenCore(mainBackStack: NavBackStack<NavKey>, isOffline: Boolean) {
    val bottomBarBackStack = rememberNavBackStack(Route.Home.Main)
    val topAppBarTexts = mapOf(
        Route.Home.Main to "Home",
        Route.Home.Search to "Search",
        Route.Home.Profile to "You"
    )

    val searchUsersViewModel: SearchUsersViewModel = koinViewModel()
    val searchUsersUiState by searchUsersViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(topAppBarTexts[bottomBarBackStack.last()]!!, isOffline) },
        bottomBar = {
            BottomBar(
                currentScreen = bottomBarBackStack.last() as Route,
                onScreenChange = {
                    bottomBarBackStack.add(1, it)
                    try {
                        bottomBarBackStack.removeAt(2)
                    } catch (_: Exception) {
                    }
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = bottomBarBackStack,
            entryProvider = entryProvider {
                entry<Route.Home.Main> {
                    MainHomeScreen(
                        onUserClick = {
                            mainBackStack.add(
                                Route.Messaging(
                                    it.uid,
                                    it.displayName,
                                    it.profilePictureUrl,
                                    it.status
                                )
                            )
                        },
                        isOffline = isOffline,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                entry<Route.Home.Search> {
                    SearchUsersScreen(
                        uiState = searchUsersUiState,
                        updateState = searchUsersViewModel::updateState,
                        onSearch = { searchUsersViewModel.getAndUpdateUsers() },
                        onUserClick = {
                            mainBackStack.add(
                                Route.Messaging(
                                    it.uid,
                                    it.displayName,
                                    it.profilePictureUrl,
                                    it.status
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                entry<Route.Home.Profile> {
                    val firebaseUser = Firebase.auth.currentUser!!
                    val user = User(
                        firebaseUser.uid,
                        firebaseUser.displayName!!,
                        firebaseUser.photoUrl.toString().removeSuffix("=s96-c"),
                        emptyList(),
                        User.Status.Online
                    )

                    ProfileScreen(
                        user = user,
                        innerPadding = innerPadding
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(text: String, isOffline: Boolean) {
    TopAppBar(
        title = { Text(text) },
        actions = {
            if (isOffline) {
                Icon(
                    imageVector = Icons.Default.WifiOff,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}

data class BottomBarIcon(
    val filled: ImageVector,
    val outlined: ImageVector,
    val text: String,
    val route: Route
)

@Composable
fun BottomBar(
    currentScreen: Route,
    onScreenChange: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier.fillMaxWidth()
    ) {
        listOf(
            BottomBarIcon(
                filled = Icons.Default.Home,
                outlined = Icons.Outlined.Home,
                text = "Home",
                route = Route.Home.Main
            ),
            BottomBarIcon(
                filled = Icons.Default.Search,
                outlined = Icons.Outlined.Search,
                text = "Search",
                route = Route.Home.Search
            ),
            BottomBarIcon(
                filled = Icons.Default.AccountCircle,
                outlined = Icons.Outlined.AccountCircle,
                text = "You",
                route = Route.Home.Profile
            ),
        ).forEach { icon ->
            NavigationBarItem(
                selected = currentScreen == icon.route,
                onClick = { onScreenChange(icon.route) },
                label = { Text(icon.text) },
                icon = {
                    Icon(
                        imageVector = if (currentScreen == icon.route) icon.filled else icon.outlined,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
