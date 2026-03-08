package dev.jason.app.compose.messaging

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.jason.app.compose.messaging.ui.AppViewModel
import dev.jason.app.compose.messaging.ui.ChatScreen
import dev.jason.app.compose.messaging.ui.EnterTokenDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExampleMessagingComposable() {
    val viewModel: AppViewModel = koinViewModel()

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        val uiState by viewModel.uiState.collectAsState()

        if (uiState.isEnteringToken) {
            EnterTokenDialog(
                token = uiState.remoteToken,
                onAction = viewModel::onAction
            )
        } else {
            ChatScreen(
                messageText = uiState.messageText,
                onAction = viewModel::onAction
            )
        }
    }
}