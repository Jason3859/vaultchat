package dev.jason.app.compose.vaultchat.messaging.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
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
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(isOffline: Boolean, viewModel: HomeViewModel) {
    val mainBackStack = rememberNavBackStack(Route.Home)
    val snackbarHostState = SnackbarHostState()

    LaunchedEffect(true) {
        SnackbarController.flow.collect { string ->
            snackbarHostState.showSnackbar(string)
        }
    }

    LaunchedEffect(true) {
        viewModel.navEvent
            .filterNotNull()
            .collect { intent ->
                val destination = intent.getStringExtra("nav_destination")
                val id = intent.getStringExtra("id")
                if (destination == "messaging" && id != null) {
                    mainBackStack.add(Route.Messaging(id))
                    viewModel.clearNavEvent()
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
                        otherUserUid = it.uid,
                        onBackClick = {
                            MessagingState.updateOtherUserId(null)
                            mainBackStack.removeLastOrNull()
                        },
                        isOffline = isOffline,
                        onUserInfoClick = { user ->
                            mainBackStack.add(
                                Route.UserInfo(
                                    user.uid,
                                    user.displayName,
                                    user.profilePictureUrl
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

    val firebaseUser = Firebase.auth.currentUser!!
    val user = User(
        firebaseUser.uid,
        firebaseUser.displayName!!,
        firebaseUser.photoUrl.toString().removeSuffix("=s96-c"),
        emptyList(),
        User.Status.Online
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(topAppBarTexts[bottomBarBackStack.last()]!!, isOffline) },
        bottomBar = {
            BottomBar(
                currentScreen = bottomBarBackStack.last() as Route,
                profilePictureUrl = user.profilePictureUrl,
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
                                Route.Messaging(it.uid)
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
                                Route.Messaging(it.uid)
                            )
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }

                entry<Route.Home.Profile> {
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
    val filled: ImageVector?,
    val outlined: ImageVector?,
    val text: String,
    val route: Route
)

@Composable
fun BottomBar(
    currentScreen: Route,
    onScreenChange: (Route) -> Unit,
    profilePictureUrl: String,
    modifier: Modifier = Modifier
) {
    val imageModifier = modifier
        .size(25.dp)
        .clip(CircleShape)

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
                filled = null,
                outlined = null,
                text = "You",
                route = Route.Home.Profile
            ),
        ).forEach { icon ->
            NavigationBarItem(
                selected = currentScreen == icon.route,
                onClick = { onScreenChange(icon.route) },
                label = { Text(icon.text) },
                icon = {
                    if (icon.filled != null && icon.outlined != null) {
                        Icon(
                            imageVector = if (currentScreen == icon.route) icon.filled else icon.outlined,
                            contentDescription = null,
                            modifier = imageModifier
                        )
                    } else {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(profilePictureUrl)
                                .crossfade(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = null,
                            modifier = imageModifier,
                            error = {
                                Log.e("HomeScreen", "BottomBar: error while loading image", it.result.throwable)

                                Icon(
                                    imageVector = if (currentScreen == icon.route) Icons.Default.AccountCircle else Icons.Outlined.AccountCircle,
                                    contentDescription = null,
                                    modifier = imageModifier
                                )
                            }
                        )
                    }
                }
            )
        }
    }
}
