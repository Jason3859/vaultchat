package dev.jason.app.compose.vaultchat.core.messaging

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.main.MainHomeScreen
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.messaging.MessagingScreen
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.nav.Route
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.profile.ProfileScreen
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.search.SearchUsersScreen
import dev.jason.app.compose.vaultchat.core.messaging.ui.screen.search.SearchUsersViewModel
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {

    val mainBackStack = rememberNavBackStack(Route.Home)

    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(true) {
        SnackbarController.flow.collect { string ->
            snackbarHostState.showSnackbar(string)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavDisplay(
            backStack = mainBackStack,
            entryProvider = entryProvider {
                entry<Route.Home> {
                    HomeScreenCore(mainBackStack)
                }

                entry<Route.Messaging> {
                    MessagingScreen(
                        otherUser = User(it.uid, it.displayName, it.profilePictureUrl),
                        onBackClick = { mainBackStack.removeLastOrNull() },
                    )
                }
            }
        )
    }
}

@Composable
private fun HomeScreenCore(mainBackStack: NavBackStack<NavKey>) {
    val bottomBarBackStack = rememberNavBackStack(Route.Home.Main)
    val topAppBarTexts = mapOf(
        Route.Home.Main to "Home",
        Route.Home.Search to "Search",
        Route.Home.Profile to "You"
    )

    val searchUsersViewModel: SearchUsersViewModel = koinViewModel()
    val searchUsersUiState by searchUsersViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(topAppBarTexts[bottomBarBackStack.last()]!!) },
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onUserClick = { mainBackStack.add(Route.Messaging(it.uid, it.displayName, it.profilePictureUrl)) },
                    )
                }

                entry<Route.Home.Search> {
                    SearchUsersScreen(
                        uiState = searchUsersUiState,
                        updateState = searchUsersViewModel::updateState,
                        onSearch = { searchUsersViewModel.getAndUpdateUsers() },
                        onUserClick = { mainBackStack.add(Route.Messaging(it.uid, it.displayName, it.profilePictureUrl)) },
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
                        firebaseUser.photoUrl.toString().removeSuffix("=s96-c")
                    )

                    ProfileScreen(
                        user = user,
                        innerPadding = innerPadding,
                        onLogOutClick = {}
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(text: String) {
    TopAppBar(
        title = { Text(text) },
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

@Preview
@Composable
private fun HomeScreenPreview() {
    VaultChatTheme {
        HomeScreen()
    }
}