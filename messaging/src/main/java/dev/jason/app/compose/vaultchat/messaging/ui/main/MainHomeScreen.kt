package dev.jason.app.compose.vaultchat.messaging.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainHomeScreen(
    viewModel: MainHomeViewModel = koinViewModel(),
    onUserClick: (User) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val connections by viewModel.connections.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(120.dp)
            )
        }
    } else {
        if (connections.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No recent connections.",
                    fontSize = 30.sp
                )
            }
        } else {
            LazyColumn(modifier = modifier.padding(vertical = 8.dp)) {
                items(connections) { user ->
                    ConnectionsItem(user, onUserClick)
                }
            }
        }
    }
}

@Composable
private fun ConnectionsItem(user: User, onUserClick: (User) -> Unit) {
    ListItem(
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.profilePictureUrl)
                        .crossfade(true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    error = {
                        Log.w(
                            "MainHomeScreen",
                            "MainHomeScreen: error while loading image",
                            it.result.throwable
                        )

                        Icon(Icons.Default.AccountCircle, null)
                    }
                )

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(user.displayName)
                    Text("Currently ${user.status}", fontSize = 12.sp)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onUserClick(user) }
    )
}

@Preview
@Composable
private fun ConnectionsItemPreview() {
    VaultChatTheme {
        ConnectionsItem(
            user = User("uid", "display name", "url", emptyList(), User.Status.Online),
            onUserClick = {  }
        )
    }
}
