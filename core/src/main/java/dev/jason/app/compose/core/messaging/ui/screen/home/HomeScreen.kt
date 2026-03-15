package dev.jason.app.compose.core.messaging.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.core.messaging.domain.model.User
import dev.jason.app.compose.core.messaging.ui.screen.main.MainHomeScreen
import dev.jason.app.compose.core.messaging.ui.screen.nav.Route
import dev.jason.app.compose.core.messaging.ui.screen.profile.ProfileScreen
import dev.jason.app.compose.core.messaging.ui.screen.search.SearchUsersScreen
import dev.jason.app.compose.core.messaging.ui.screen.search.SearchUsersViewModel
import dev.jason.app.compose.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen() {

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
                            .padding(innerPadding)
                    )
                }

                entry<Route.Home.Search> {
                    SearchUsersScreen(
                        uiState = searchUsersUiState,
                        updateState = searchUsersViewModel::updateState,
                        onSearch = { searchUsersViewModel.getAndUpdateUsers() },
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
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
}

@Preview
@Composable
private fun HomeScreenPreview() {
    VaultChatTheme {
        HomeScreen()
    }
}