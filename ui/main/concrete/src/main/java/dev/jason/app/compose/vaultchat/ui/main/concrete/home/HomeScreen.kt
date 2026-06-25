package dev.jason.app.compose.vaultchat.ui.main.concrete.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.ui.main.abstractt.home.AbstractHomeScreen
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onUserClick: (UserUi) -> Unit,
    onNonConnectedUserClick: (UserUi) -> Unit,
    onProfileClick: () -> Unit
) {
    val viewModel: HomeViewModel = koinViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by AppState.isOnline.collectAsStateWithLifecycle()
    val selectedUser by AppState.otherUser.collectAsStateWithLifecycle()
    val currentUser by AppState.currentUser.collectAsStateWithLifecycle()
    val currentUserProfilePictureUrl = currentUser?.profilePictureUrl!!

    AbstractHomeScreen(
        isOffline = !isOnline,
        selectedUserUid = selectedUser?.uid,
        uiState = uiState,
        currentUserProfilePictureUrl = currentUserProfilePictureUrl,
        onAction = viewModel::onAction,
        onUserClick = onUserClick,
        onProfileClick = onProfileClick,
        onNonConnectedUserClick = onNonConnectedUserClick,
    )
}