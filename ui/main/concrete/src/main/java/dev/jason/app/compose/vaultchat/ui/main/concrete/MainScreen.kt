package dev.jason.app.compose.vaultchat.ui.main.concrete

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.ui.main.concrete.home.HomeScreen
import dev.jason.app.compose.vaultchat.ui.main.concrete.messaging.MessagingScreen
import dev.jason.app.compose.vaultchat.ui.main.concrete.nav.Route
import dev.jason.app.compose.vaultchat.ui.main.concrete.profile.ProfileScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainScreen(
    backStack: SnapshotStateList<Route>,
    onBack: () -> Unit,
    navigate: (Route) -> Unit,
    onBackToHome: () -> Unit
) {

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
                    onUserClick = { navigate(Route.Messaging(it.uid)) },
                    onNonConnectedUserClick = { user ->
                        navigate(Route.Messaging.fromUser(user))
                    },
                    onProfileClick = { navigate(Route.Profile) }
                )
            }

            entry<Route.Messaging>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) { route ->
                val user = UserUi(route.uid)
                MessagingScreen(
                    otherUser = user,
                    onBackClick = onBackToHome,
                    onUserInfoClick = {
                        navigate(Route.Profile)
                    }
                )
            }

            entry<Route.Profile>(
//                metadata = if (Device.getDeviceType(context) == Device.Type.Tablet) {
//                    ListDetailSceneStrategy.extraPane()
//                } else {
//                    ListDetailSceneStrategy.detailPane()
//                }
                metadata = ListDetailSceneStrategy.extraPane()
            ) {
                ProfileScreen(
                    onBack = onBack
                )
            }
        }
    )
}