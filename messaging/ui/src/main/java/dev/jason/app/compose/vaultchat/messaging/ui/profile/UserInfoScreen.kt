package dev.jason.app.compose.vaultchat.messaging.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UserInfoScreen(
    user: User,
    onBackClick: () -> Unit, // for icon button. goes back to MessagingScreen
    navigateBackToHomeScreen: () -> Unit // goes back to HomeScreen instead of MessagingScreen
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()

    val showClearChatHistoryWarning = retain { mutableStateOf(false) }
    val showBlockUserWarning = retain { mutableStateOf(false) }

    Box {
        UserInfoScreen(
            user = user,
            onBackClick = onBackClick,
            onBlockClick = {
                showBlockUserWarning.value = true
            },
            onClearHistoryClick = {
                showClearChatHistoryWarning.value = true
            }
        )

        if (showBlockUserWarning.value) {
            AlertDialog(
                onDismissRequest = { showBlockUserWarning.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.blockUser(user, navigateBackToHomeScreen)
                        }
                    ) {
                        Text("Conform")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showBlockUserWarning.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text("Block user?")
                },
                text = {
                    Text("You can unblock user from your profile screen.")
                }
            )
        }

        if (showClearChatHistoryWarning.value) {
            AlertDialog(
                onDismissRequest = { showClearChatHistoryWarning.value = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteMessageHistory(user)
                            onBackClick()
                        }
                    ) {
                        Text("Conform")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            showClearChatHistoryWarning.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                },
                title = {
                    Text("Clear chat history with ${user.displayName}?")
                },
                text = {
                    Text("This action cannot be undone.")
                }
            )
        }
    }
}

@Composable
private fun UserInfoScreen(
    user: User,
    onBackClick: () -> Unit,
    onBlockClick: () -> Unit,
    onClearHistoryClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(user.profilePictureUrl)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = null,
                error = {
                    Log.w(
                        "UserInfoScreen",
                        "UserInfoScreen: error while loading image",
                        it.result.throwable
                    )

                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()

                    Image(Icons.Default.AccountCircle, null)
                },
                loading = {
                    Image(Icons.Default.AccountCircle, null)
                },
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
            )

            Text(
                text = user.displayName,
                fontSize = 33.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    icon = Icons.Default.Block,
                    text = "Block",
                    onClick = onBlockClick,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )

                ActionButton(
                    icon = Icons.Default.DeleteForever,
                    text = "Clear chat history",
                    onClick = onClearHistoryClick,
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = containerColor),
        border = ButtonDefaults.outlinedButtonBorder(),
        shape = RoundedCornerShape(15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null)
            Text(text, textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
private fun UserInfoScreenPreview() {
    VaultChatTheme {
        UserInfoScreen(
            user = User("uid", "Display Name", "url", emptyList(), User.Status.Online),
            onBackClick = {},
            onBlockClick = {},
            onClearHistoryClick = {}
        )
    }
}