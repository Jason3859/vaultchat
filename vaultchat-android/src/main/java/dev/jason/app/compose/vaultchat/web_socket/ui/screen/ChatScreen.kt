package dev.jason.app.compose.vaultchat.web_socket.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.web_socket.ui.model.MessageUiState
import dev.jason.app.compose.vaultchat.web_socket.ui.model.UiMessage
import dev.jason.app.compose.vaultchat.web_socket.ui.model.UiUser
import dev.jason.app.compose.vaultchat.web_socket.ui.nav.WebSocketConnectionRoute

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ChatScreen() {
    val backStack = rememberNavBackStack(WebSocketConnectionRoute.HomeScreen)

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }

    val listDetailSceneStrategy = rememberListDetailSceneStrategy<Any>(directive = directive)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = listDetailSceneStrategy,
        entryProvider = entryProvider {
            entry<WebSocketConnectionRoute.HomeScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = { DetailScreenPlaceHolder() }
                )
            ) {
                HomeScreen(
                    rooms = List(10) { id -> "id: $id" },
                    onCardClick = { backStack.navToDetails(it) },
                    onNewConnectionClick = {},
                    modifier = Modifier.fillMaxSize()
                )
            }

            entry<WebSocketConnectionRoute.MessagingScreen>(
                metadata = ListDetailSceneStrategy.detailPane()
            ) {
                MessagingScreen(
                    messages = List(20) { index ->
                        UiMessage(
                            id = index.toString(),
                            roomId = it.id,
                            user = UiUser("me"),
                            text = "hello @$index",
                            timestamp = "timestamp",
                        )
                    },
                    onBackClick = { backStack.removeLastOrNull() },
                    onSendClick = {},
                    uiState = MessageUiState("hello"),
                    onMessageChange = {}
                )
            }
        }
    )
}

fun NavBackStack<NavKey>.navToDetails(id: String) {
    if (last() is WebSocketConnectionRoute.MessagingScreen) {
        add(WebSocketConnectionRoute.MessagingScreen(id))
        removeAt(lastIndex - 1)
    } else {
        add(WebSocketConnectionRoute.MessagingScreen(id))
    }
}