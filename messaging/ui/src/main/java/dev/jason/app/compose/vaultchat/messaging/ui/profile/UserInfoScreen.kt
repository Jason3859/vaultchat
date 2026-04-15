package dev.jason.app.compose.vaultchat.messaging.ui.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    onBackClick: () -> Unit,
    navigateBackToHomeScreen: () -> Unit
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()

    UserInfoScreen(
        user = user,
        onBackClick = onBackClick,
        onBlockClick = {
            viewModel.blockUser(it, navigateBackToHomeScreen)
        }
    )
}

@Composable
fun UserInfoScreen(
    user: User,
    onBackClick: () -> Unit,
    onBlockClick: (User) -> Unit
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
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { onBlockClick(user) },
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    border = ButtonDefaults.outlinedButtonBorder(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Block, null)
                        Text("Block")
                    }
                }
            }
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
            onBlockClick = {}
        )
    }
}