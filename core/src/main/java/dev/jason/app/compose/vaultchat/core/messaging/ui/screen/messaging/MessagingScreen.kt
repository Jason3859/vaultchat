package dev.jason.app.compose.vaultchat.core.messaging.ui.screen.messaging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.User
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MessagingScreen(
    otherUser: User,
    onBackClick: () -> Unit,
) {
    val viewmodel: MessagingViewModel = koinViewModel {
        parametersOf(otherUser)
    }

    val uiState by viewmodel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(viewmodel.messages) {
        if (viewmodel.messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(viewmodel.messages.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(otherUser, onBackClick)
        },
        bottomBar = {
            BottomBar(uiState, viewmodel::updateState, viewmodel::sendMessage)
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(viewmodel.messages) { msg ->
                Column(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalAlignment = if (otherUser.uid == msg.from) Alignment.Start else Alignment.End
                ) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                viewmodel.pendingMessages.contains(msg) -> Color.Cyan
                                viewmodel.failedMessages.contains(msg) -> MaterialTheme.colorScheme.errorContainer
                                else -> Color.Unspecified // default color
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                        ) {
                            Text(msg.text)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    otherUser: User,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(otherUser.profilePictureUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                )

                Spacer(Modifier.width(10.dp))

                Text(otherUser.displayName)
            }
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    )
}

@Composable
private fun BottomBar(
    uiState: MessagingViewModel.UiState,
    updateState: (MessagingViewModel.UiState) -> Unit,
    onSend: () -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent
    ) {
        OutlinedTextField(
            value = uiState.messageText,
            onValueChange = { updateState(uiState.copy(messageText = it)) },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(8.dp)
                .height(55.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            shape = CircleShape,
            placeholder = { Text("Message") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onSend() }
            )
        )

        IconButton(
            onClick = onSend,
            colors = IconButtonDefaults.filledTonalIconButtonColors(),
            shape = CircleShape,
            modifier = Modifier.size(55.dp),
            enabled = uiState.sendButtonEnabled
        ) {
            Icon(Icons.AutoMirrored.Default.Send, null)
        }
    }
}
