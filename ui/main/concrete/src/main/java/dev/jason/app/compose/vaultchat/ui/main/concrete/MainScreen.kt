package dev.jason.app.compose.vaultchat.ui.main.concrete

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.NavEvent
import dev.jason.app.compose.vaultchat.ui.main.concrete.home.HomeScreen
import dev.jason.app.compose.vaultchat.ui.main.concrete.messaging.MessagingScreen
import dev.jason.app.compose.vaultchat.ui.main.concrete.nav.Route
import dev.jason.app.compose.vaultchat.ui.main.concrete.profile.ProfileScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen() {
    val backStack = rememberNavBackStack(Route.Home)
    val onBack: () -> Unit = {
        if (backStack.last() is Route.Messaging) {
            AppState.updateOtherUser(null)
        }

        // only clear last entry of backStack
        // if user is not at HomeScreen.
        // app is crashing otherwise
        if (backStack.last() !is Route.Home) {
            backStack.removeLastOrNull()
        }
    }

    LaunchedEffect(true) {
        AppState.navEvent.collect { navEvent ->
            if (navEvent is NavEvent.NavigateToMessagingScreen) {
                backStack.add(Route.Messaging(navEvent.uid))
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        sceneStrategies = listOf(rememberListDetailSceneStrategy()),
        entryProvider = entryProvider {
            entry<Route.Home>(
                metadata = ListDetailSceneStrategy.listPane()
            ) {
                HomeScreen(
                    onUserClick = { backStack.add(Route.Messaging(it.uid)) },
                    onProfileClick = { backStack.add(Route.Profile) }
                )
            }

            entry<Route.Messaging>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                MessagingScreen(
                    uid = it.uid,
                    onBackClick = onBack,
                    onUserInfoClick = {
                        backStack.add(Route.Profile)
                    }
                )
            }

            entry<Route.Profile>(
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                ProfileScreen(
                    onBack = onBack
                )
            }
        }
    )
}