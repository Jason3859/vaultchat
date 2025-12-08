package dev.jason.app.compose.vaultchat.web_socket.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme
import dev.jason.app.compose.vaultchat.web_socket.ui.model.MessageUiState
import dev.jason.app.compose.vaultchat.web_socket.ui.model.UiMessage
import dev.jason.app.compose.vaultchat.web_socket.ui.model.UiUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    messages: List<UiMessage>,
    onBackClick: () -> Unit,
    onSendClick: () -> Unit,
    uiState: MessageUiState,
    onMessageChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            lazyListState.animateScrollToItem(messages.lastIndex - 1)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(messages.first().roomId) },
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(painterResource(R.drawable.arrow_back), null)
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.message,
                    onValueChange = onMessageChange,
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = CircleShape
                )

                IconButton(
                    onClick = onSendClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                ) {
                    Icon(painterResource(R.drawable.send), null)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            state = lazyListState
        ) {
            items(messages) { message ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            message.user.let { user ->
                                user.profilePictureUrl?.let { url ->
                                    AsyncImage(
                                        model = url,
                                        contentDescription = null
                                    )
                                }

                                Text(
                                    text = user.name,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = message.text,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun MessagingScreenPreview() {
    MessengerTheme {
        MessagingScreen(
            messages = List(20) { index ->
                UiMessage(
                    id = index.toString(),
                    roomId = "hello",
                    user = UiUser("me"),
                    text = "hello @$index",
                    timestamp = "timestamp",
                )
            },
            onBackClick = {},
            onSendClick = {},
            uiState = MessageUiState("hello"),
            onMessageChange = {}
        )
    }
}