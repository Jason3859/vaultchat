package dev.jason.app.compose.vaultchat.messaging.ui.messaging

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.WifiOff
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDateTime

@Composable
fun MessagingScreen(
    otherUser: User,
    onBackClick: () -> Unit,
    isOffline: Boolean
) {
    val viewModel: MessagingViewModel = koinViewModel { parametersOf(otherUser) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MessagingScreen(
        otherUser = otherUser,
        onBackClick = onBackClick,
        uiState = uiState,
        updateState = viewModel::updateState,
        sendMessage = viewModel::sendMessage,
        messages = viewModel.messages,
        pendingMessages = viewModel.pendingMessages,
        failedMessages = viewModel.failedMessages,
        isOffline = isOffline
    )
}

@Composable
private fun MessagingScreen(
    otherUser: User,
    onBackClick: () -> Unit,
    uiState: MessagingViewModel.UiState,
    updateState: (MessagingViewModel.UiState) -> Unit,
    sendMessage: () -> Unit,
    messages: List<Message>,
    pendingMessages: List<Message>,
    failedMessages: List<Message>,
    isOffline: Boolean
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(otherUser, onBackClick, isOffline)
        },
        bottomBar = {
            BottomBar(uiState, updateState, sendMessage)
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { Spacer(Modifier.height(6.dp)) }
            items(messages) { msg ->
                Column(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalAlignment = if (otherUser.uid == msg.from) Alignment.Start else Alignment.End
                ) {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(top = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                pendingMessages.contains(msg) -> Color.Cyan
                                failedMessages.contains(msg) -> MaterialTheme.colorScheme.errorContainer
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

                    Text(
                        text = if (pendingMessages.contains(msg)) "Sending" else msg.timestamp.display(),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

private fun LocalDateTime.display() =
    "${dayOfMonth}/${month.value}/${year % 100} ${hour}:${if (minute >= 10) minute else minute.toString().padStart(2, '0')}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    otherUser: User,
    onBackClick: () -> Unit,
    isOffline: Boolean
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(otherUser.profilePictureUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp),
                    error = {
                        Icon(Icons.Default.AccountCircle, null)
                        Toast.makeText(context, "Error loading image", Toast.LENGTH_SHORT).show()
                    }
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(otherUser.displayName)
                    Text(
                        text = if (isOffline) "You are offline" else "Currently ${otherUser.status}",
                        fontSize = 12.sp
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
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

@Preview
@Composable
private fun MessagingScreenPreview() {
    val me = "me"
    val other = "other-user"

    val messages = List(20) { index ->
        Message(
            from = if (index % 2 == 0) me else other,
            to = if (index % 2 == 0) other else me,
            text = "text-$index",
            timestamp = LocalDateTime.parse("2026-04-01T11:02:10.692796400")
        )
    }

    VaultChatTheme {
        MessagingScreen(
            otherUser = User(other, other, other, emptyList(), User.Status.Online),
            onBackClick = {},
            uiState = MessagingViewModel.UiState(),
            updateState = {},
            sendMessage = {},
            messages = messages,
            pendingMessages = messages.subList(0, 5),
            failedMessages = messages.subList(10, 15),
            isOffline = false
        )
    }
}