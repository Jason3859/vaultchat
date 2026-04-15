package dev.jason.app.compose.vaultchat.messaging.ui.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.domain.Device
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import org.koin.androidx.compose.koinViewModel

data class ProfileScreenRowItem(
    val id: ProfileScreenRowItemId,
    val label: String,
    val icon: ImageVector,
    val action: () -> Unit
)

enum class ProfileScreenRowItemId {
    Devices, Blocklist, Logout
}

@Composable
fun ProfileScreen(
    user: User,
    innerPadding: PaddingValues,
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val blocklist by viewModel.blocklist.collectAsStateWithLifecycle()

    ProfileScreen(
        user = user,
        devices = devices,
        blocklist = blocklist,
        onUnblockClick = viewModel::unblockUser,
        onLogOutClick = viewModel::logout,
        innerPadding = innerPadding
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileScreen(
    user: User,
    devices: List<Device>,
    blocklist: List<User>,
    onUnblockClick: (User) -> Unit,
    onLogOutClick: () -> Unit,
    innerPadding: PaddingValues
) {
    var showLogoutDialog by retain { mutableStateOf(false) }
    var currentProfileScreenRowItemId by retain { mutableStateOf<ProfileScreenRowItemId?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Box {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.profilePictureUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        error = {
                            Log.w(
                                "ProfileScreen",
                                "ProfileScreen: failed to load image",
                                it.result.throwable
                            )

                            Image(Icons.Default.AccountCircle, null)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(150.dp)
                    )
                }

                item {
                    Text(
                        text = user.displayName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(
                            ProfileScreenRowItem(
                                id = ProfileScreenRowItemId.Devices,
                                label = "Your Devices",
                                icon = Icons.Default.Devices,
                                action = {
                                    currentProfileScreenRowItemId = ProfileScreenRowItemId.Devices
                                }
                            ),
                            ProfileScreenRowItem(
                                id = ProfileScreenRowItemId.Blocklist,
                                label = "Blocklist",
                                icon = Icons.Default.Block,
                                action = {
                                    currentProfileScreenRowItemId = ProfileScreenRowItemId.Blocklist
                                }
                            ),
                            ProfileScreenRowItem(
                                id = ProfileScreenRowItemId.Logout,
                                label = "Logout",
                                icon = Icons.AutoMirrored.Filled.Logout,
                                action = { showLogoutDialog = true }
                            )
                        ).forEach { item ->
                            OutlinedButton(
                                onClick = {
                                    item.action()
                                },
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = when {
                                        item.id == ProfileScreenRowItemId.Logout -> MaterialTheme.colorScheme.errorContainer
                                        currentProfileScreenRowItemId == item.id -> MaterialTheme.colorScheme.primaryContainer
                                        else -> Color.Unspecified
                                    }
                                )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(item.icon, null)
                                    Text(item.label)
                                }
                            }
                        }
                    }
                }

                when (currentProfileScreenRowItemId) {
                    ProfileScreenRowItemId.Devices -> {
                        items(devices) { device ->
                            DeviceItem(device)
                        }
                    }

                    ProfileScreenRowItemId.Blocklist -> {
                        if (blocklist.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillParentMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No blocked users",
                                        fontSize = 20.sp
                                    )
                                }
                            }
                        } else {
                            items(blocklist) { blocklistUser ->
                                BlocklistItem(blocklistUser, onUnblockClick)
                            }
                        }

                    }

                    else -> {}
                }
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    confirmButton = {
                        TextButton(onLogOutClick) {
                            Text("Logout")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = {
                        Text("Logout?")
                    },
                    text = {
                        Text("You'll have to log in again.")
                    }
                )
            }
        }
    }
}

@Composable
private fun ProfileScreenItemContainer(content: @Composable RowScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(
                border = ButtonDefaults.outlinedButtonBorder(),
                shape = RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
private fun DeviceItem(device: Device) {
    ProfileScreenItemContainer {
        Icon(
            imageVector = when (device.type) {
                Device.Type.Mobile -> {
                    Icons.Default.PhoneAndroid
                }

                else -> {
                    Icons.Default.Tablet
                }
            },
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )

        Column {
            Text(
                text = if (device == Device.getCurrentDevice(
                        LocalContext.current, "not required"
                    )
                ) "${device.name} (current)" else device.name,
                fontSize = 18.sp
            )
            Text("${device.os} ${device.version}")
        }
    }
}

@Composable
fun BlocklistItem(user: User, onUnblockClick: (User) -> Unit) {
    ProfileScreenItemContainer {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.profilePictureUrl)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .crossfade(true)
                .build(),
            contentDescription = null,
            error = {
                Log.w(
                    "ProfileScreen",
                    "ProfileScreen: failed to load image",
                    it.result.throwable
                )

                Image(Icons.Default.AccountCircle, null)
            },
            loading = {
                Image(Icons.Default.AccountCircle, null)
            },
            modifier = Modifier.size(40.dp)
        )

        Text(user.displayName, fontSize = 20.sp)

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OutlinedButton(
                onClick = { onUnblockClick(user) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Text("Unblock")
            }
        }
    }

}

object ProfileScreen {
    val defaultDeviceList = List(10) {
        Device(
            name = "device",
            type = Device.Type.Mobile,
            os = Device.Os.Android,
            version = "13",
            fcmToken = "not required"
        )
    }

    val defaultUserList = List(10) { index ->
        User(
            uid = "uid",
            displayName = "display name @$index",
            profilePictureUrl = "",
            devices = emptyList(),
            status = User.Status.Online
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    VaultChatTheme {
        ProfileScreen(
            user = User("name", "name", "url", emptyList(), User.Status.Online),
            innerPadding = PaddingValues(),
            onLogOutClick = {},
            devices = ProfileScreen.defaultDeviceList,
            blocklist = ProfileScreen.defaultUserList,
            onUnblockClick = {}
        )
    }
}

@Preview
@Composable
private fun MobileDeviceItemPreview() {
    VaultChatTheme {
        DeviceItem(
            device = Device("name", Device.Type.Mobile, Device.Os.Android, "11", "")
        )
    }
}

@Preview
@Composable
private fun TabletDeviceItemPreview() {
    VaultChatTheme {
        DeviceItem(
            device = Device("name", Device.Type.Tablet, Device.Os.Android, "10", "")
        )
    }
}

@Preview
@Composable
private fun BlocklistItemPreview() {
    VaultChatTheme {
        BlocklistItem(
            user = User(
                uid = "uid",
                displayName = "display name",
                profilePictureUrl = "",
                devices = emptyList(),
                status = User.Status.Online
            ),
            onUnblockClick = {}
        )
    }
}