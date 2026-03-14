package dev.jason.app.compose.core.messaging.ui.screen.messaging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.core.messaging.domain.model.Message
import dev.jason.app.compose.core.ui.theme.VaultChatTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MessagingScreen(
    onBackClick: () -> Unit,
    viewModel: MessagingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MessagingScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSend = viewModel::sendMessage,
        updateState = viewModel::updateState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MessagingScreen(
    uiState: MessagingViewModel.UiState,
    onBackClick: () -> Unit,
    onSend: () -> Unit,
    updateState: (MessagingViewModel.UiState) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(uiState.otherUserDisplayName) },
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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
                    placeholder = { Text("Message") }
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(uiState.messages) { msg ->
                Column(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalAlignment = if (uiState.currentUserUid == msg.from) Alignment.End else Alignment.Start
                ) {
                    Card(
                        modifier = Modifier
                            .padding(vertical = 6.dp, horizontal = 12.dp)
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

@PreviewLightDark
@Composable
private fun MessagingScreenPreview() {
    VaultChatTheme {
        MessagingScreen(
            onBackClick = {},
            onSend = {},
            updateState = {},
            uiState = MessagingViewModel.UiState(
                messageText = "Hello, World!",
                currentUserUid = "me too",
                otherUserUid = "me",
                otherUserDisplayName = "me",
                sendButtonEnabled = true,
                messages = List(10) { index ->
                    Message(
                        from = if (index == 3 || index == 7 || index == 0 || index == 9) "me too" else "me",
                        to = "me",
                        text = "Body: $index"
                    )
                }
            )
        )
    }
}