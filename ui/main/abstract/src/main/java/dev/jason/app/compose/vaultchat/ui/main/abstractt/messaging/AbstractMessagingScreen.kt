package dev.jason.app.compose.vaultchat.ui.main.abstractt.messaging

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.MessageUi
import dev.jason.app.compose.vaultchat.ui.main.abstractt.model.UserUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Immutable
data class MessagePagingState(
    val data: Flow<PagingData<MessageUi>>
)

@Composable
fun AbstractMessagingScreen(
    otherUser: UserUi,
    onBackClick: () -> Unit,
    uiState: MessagingUiState,
    onAction: (MessagingUiAction) -> Unit,
    onUserInfoClick: () -> Unit,
    messages: MessagePagingState,
    pendingMessages: ImmutableList<MessageUi>,
    failedMessages: ImmutableList<MessageUi>,
    isOffline: Boolean
) {
    val pagingItems = messages.data.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()

    val totalItemCount = pagingItems.itemCount + pendingMessages.size + failedMessages.size

    LaunchedEffect(totalItemCount) {
        if (totalItemCount > 0) {
            lazyListState.animateScrollToItem(totalItemCount - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(otherUser, onBackClick, onUserInfoClick, isOffline)
        },
        bottomBar = {
            BottomBar(uiState, onAction)
        }
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item { Spacer(Modifier.height(6.dp)) }

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.timestamp }
            ) { index ->
                val msg = pagingItems[index] ?: return@items
                MessageItem(
                    msg = msg,
                    isOtherUser = otherUser.uid == msg.from,
                    status = MessageStatus.Sent
                )
            }

            items(pendingMessages) { msg ->
                MessageItem(
                    msg = msg,
                    isOtherUser = otherUser.uid == msg.from,
                    status = MessageStatus.Pending
                )
            }

            items(failedMessages) { msg ->
                MessageItem(
                    msg = msg,
                    isOtherUser = otherUser.uid == msg.from,
                    status = MessageStatus.Failed
                )
            }
        }
    }
}

private enum class MessageStatus {
    Sent, Pending, Failed
}

@Composable
private fun MessageItem(
    msg: MessageUi,
    isOtherUser: Boolean,
    status: MessageStatus
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOtherUser) Alignment.Start else Alignment.End
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = when (status) {
                    MessageStatus.Pending -> Color.Cyan
                    MessageStatus.Failed -> MaterialTheme.colorScheme.errorContainer
                    MessageStatus.Sent -> Color.Unspecified // default color
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
            text = when (status) {
                MessageStatus.Pending -> "Sending"
                MessageStatus.Failed -> "Failed"
                MessageStatus.Sent -> msg.timestamp
            },
            fontSize = 10.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    otherUser: UserUi,
    onBackClick: () -> Unit,
    onUserInfoClick: () -> Unit,
    isOffline: Boolean
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserInfoClick() }
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
                        ToastController.showToast("Error loading image")
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
    uiState: MessagingUiState,
    onAction: (MessagingUiAction) -> Unit
) {
    BottomAppBar(
        containerColor = Color.Transparent
    ) {
        OutlinedTextField(
            value = uiState.messageText,
            onValueChange = { newValue ->
                onAction(MessagingUiAction.UpdateState {
                    it.copy(messageText = newValue)
                })
            },
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
                onSend = { onAction(MessagingUiAction.SendMessage) }
            )
        )

        IconButton(
            onClick = { onAction(MessagingUiAction.SendMessage) },
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
private fun AbstractMessagingScreenPreview() {
    val me = "me"
    val other = "other-user"

    val list = List(20) { index ->
        MessageUi(
            from = if (index % 2 == 0) me else other,
            to = if (index % 2 == 0) other else me,
            text = "text-$index",
            timestamp = "2026-04-01T11:02:10.692796400"
        )
    }
    
    val messagesFlow = flowOf(PagingData.from(list))

    VaultChatTheme {
        AbstractMessagingScreen(
            otherUser = UserUi(other, other, other, User.Status.Online),
            onBackClick = {},
            uiState = MessagingUiState(),
            onAction = {},
            messages = MessagePagingState(messagesFlow),
            pendingMessages = list.subList(0, 5).toImmutableList(),
            failedMessages = list.subList(10, 15).toImmutableList(),
            isOffline = false,
            onUserInfoClick = {}
        )
    }
}