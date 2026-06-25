package dev.jason.app.compose.vaultchat.ui.main.concrete.messaging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging.AbstractMessagingScreen
import dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging.MessagePagingState
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MessagingScreen(
    uid: String,
    otherUser: UserUi,
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit
) {
    val viewModel: MessagingViewModel = koinViewModel { parametersOf(uid, otherUser) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val otherUser by viewModel.otherUser.collectAsStateWithLifecycle()
    val isOnline by AppState.isOnline.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(
                modifier = Modifier.size(200.dp)
            )
        }
    } else if (otherUser != null) {
        AbstractMessagingScreen(
            otherUser = otherUser!!,
            onBackClick = onBackClick,
            uiState = uiState,
            onAction = viewModel::onAction,
            onUserInfoClick = onUserInfoClick,
            messages = MessagePagingState(viewModel.messages),
            pendingMessages = viewModel.pendingMessages.toImmutableList(),
            failedMessages = viewModel.failedMessages.toImmutableList(),
            isOffline = !isOnline
        )
    }
}
