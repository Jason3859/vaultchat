package dev.jason.app.compose.core.messaging

import androidx.compose.runtime.Composable
import dev.jason.app.compose.core.messaging.ui.screen.HomeScreen

@Composable
fun ExampleMessagingComposable() {
//    val viewModel: AppViewModel = koinViewModel()
//
//    Surface(
//        color = MaterialTheme.colorScheme.background,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        val uiState by viewModel.uiState.collectAsState()
//
//        if (uiState.isEnteringToken) {
//            EnterTokenDialog(
//                token = uiState.remoteToken,
//                onAction = viewModel::onAction
//            )
//        } else {
//            ChatScreen(
//                messageText = uiState.messageText,
//                onAction = viewModel::onAction
//            )
//        }
//    }

    HomeScreen()
}