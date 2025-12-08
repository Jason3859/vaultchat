package dev.jason.app.compose.vaultchat.web_socket.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    rooms: List<String>,
    onCardClick: (String) -> Unit,
    onNewConnectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.your_recent_connections)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                actions = {
                    IconButton(onNewConnectionClick) {
                        Icon(
                            painter = painterResource(R.drawable.add),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                items(rooms) { id ->
                    RoomCard(
                        roomId = id,
                        onCardClick = { onCardClick(id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCard(
    roomId: String,
    onCardClick: () -> Unit,
    height: Dp = 55.dp,
    padding: Dp = 8.dp
) {
    Card(
        onClick = onCardClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(padding)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .padding(start = 16.dp)
        ) {
            Text(roomId)
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    MessengerTheme {
        HomeScreen(
            rooms = List(10) { id -> "id: $id" },
            onCardClick = {},
            onNewConnectionClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}