package dev.jason.app.compose.vaultchat.ui.main.abstractt.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.device.Device
import dev.jason.app.compose.vaultchat.core.model.device.DeviceUi
import dev.jason.app.compose.vaultchat.core.model.device.toDevice
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.ui.LoadImage
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.ui.main.abstractt.R
import dev.jason.app.compose.vaultchat.ui.main.abstractt.home.HomeUiState
import kotlinx.collections.immutable.ImmutableList

enum class ProfileScreenRowItemId {
    Devices, Blocklist, Logout,
    BlockUser, DeleteAllMessages
}

data class ProfileScreenRowItem(
    val id: ProfileScreenRowItemId,
    val name: String,
    val icon: ImageVector
)

@Composable
fun AbstractProfileScreen(
    onBack: () -> Unit,
    user: UserUi,
    devices: ImmutableList<DeviceUi>? = null,
    blocklist: ImmutableList<UserUi>? = null,
    onAction: (ProfileUiAction) -> Unit
) {
    var currentDialog by remember { mutableStateOf<ProfileScreenRowItemId?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ProfileTopBar(onBack)
        }
    ) { innerPadding ->
        ProfileDialogs(
            currentDialog = currentDialog,
            onDismiss = { currentDialog = null },
            user = user,
            devices = devices,
            blocklist = blocklist,
            onAction = onAction,
            onBack = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            UserInfoSection(user)

            ActionButtonsRow(
                isOwnProfile = (devices != null && blocklist != null),
                onActionClick = { currentDialog = it }
            )
        }
    }
}

@Composable
private fun ProfileTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = onBack,
                colors = IconButtonDefaults.iconButtonVibrantColors()
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
}

@Composable
private fun ProfileDialogs(
    onBack: () -> Unit,
    currentDialog: ProfileScreenRowItemId?,
    onDismiss: () -> Unit,
    user: UserUi,
    devices: ImmutableList<DeviceUi>?,
    blocklist: ImmutableList<UserUi>?,
    onAction: (ProfileUiAction) -> Unit
) {
    when (currentDialog) {
        ProfileScreenRowItemId.Devices -> devices?.let {
            DevicesDialog(
                devices = it,
                onAction = onAction,
                onDismiss = onDismiss
            )
        }

        ProfileScreenRowItemId.Blocklist -> blocklist?.let {
            BlocklistDialog(
                blocklist = it,
                onAction = onAction,
                onDismiss = onDismiss
            )
        }

        ProfileScreenRowItemId.Logout -> LogoutDialog(
            onDismiss = onDismiss,
            onAction = onAction
        )

        ProfileScreenRowItemId.BlockUser -> BlockUserDialog(
            userDisplayName = user.displayName,
            onConfirmClick = {
                onAction(
                    ProfileUiAction.BlockUser(
                        user = user,
                        onFinish = {
                            AppEvents.sendEvent(AppEvent.NavEvent.NavigateToHomeScreen)
                        }
                    )
                )
                onDismiss()
            },
            onDismiss = onDismiss
        )

        ProfileScreenRowItemId.DeleteAllMessages -> DeleteMessagesHistoryDialog(
            displayName = user.displayName,
            onDismiss = onDismiss,
            onConfirmClick = {
                onAction(ProfileUiAction.DeleteMessagesHistory)
                onDismiss.invoke()
                onBack.invoke()
            }
        )

        null -> {}
    }
}

@Composable
private fun UserInfoSection(user: UserUi) {
    val imageModifier = Modifier
        .size(175.dp)
        .clip(CircleShape)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            LoadImage(
                url = user.profilePictureUrl,
                modifier = imageModifier
            )

            Text(
                text = user.displayName,
                fontSize = 24.sp,
                style = MaterialTheme.typography.labelMediumEmphasized
            )
        }
    }
}

@Composable
private fun ActionButtonsRow(
    isOwnProfile: Boolean,
    onActionClick: (ProfileScreenRowItemId) -> Unit
) {
    val items = if (isOwnProfile) {
        listOf(
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Devices,
                name = ProfileScreenRowItemId.Devices.toString(),
                icon = Icons.Default.Devices
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Blocklist,
                name = ProfileScreenRowItemId.Blocklist.toString(),
                icon = Icons.Default.Block
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.Logout,
                name = ProfileScreenRowItemId.Logout.toString(),
                icon = Icons.AutoMirrored.Filled.Logout
            )
        )
    } else {
        listOf(
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.BlockUser,
                name = "Block user",
                icon = Icons.Default.Block
            ),
            ProfileScreenRowItem(
                id = ProfileScreenRowItemId.DeleteAllMessages,
                name = "Delete all messages",
                icon = Icons.Default.DeleteForever
            )
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { (id, name, icon) ->
            ProfileScreenRowItem(
                name = name,
                icon = icon,
                onClick = { onActionClick(id) }
            )
        }
    }
}

@Composable
private fun ProfileScreenRowItem(
    name: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null)
            Text(name)
        }
    }
}

@Composable
fun DeleteMessagesHistoryDialog(
    displayName: String,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_all_messages_warning_title, displayName)) },
        text = { Text(stringResource(R.string.delete_all_messages_warning_text)) },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.delete_all_messages))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun BlockUserDialog(
    userDisplayName: String,
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.block_user_warning_title, userDisplayName)) },
        text = { Text(stringResource(R.string.block_user_warning_text)) },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.block))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun LogoutDialog(
    onDismiss: () -> Unit,
    onAction: (ProfileUiAction) -> Unit
) {
    val showConfirmDialog = remember { mutableStateOf(false) }

    if (!showConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.logout_warning_title)) },
            confirmButton = {
                TextButton(
                    onClick = { showConfirmDialog.value = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    } else {
        AlertDialog(
            onDismissRequest = {
                showConfirmDialog.value = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = { onAction(ProfileUiAction.LogoutCurrentDeviceByClearingMessages) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_by_clearing_messages))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onAction(ProfileUiAction.LogoutCurrentDeviceWithoutClearingMessages) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_without_clearing_messages))
                }
            },
            title = { Text(stringResource(R.string.logout_clear_messages_title)) },
            text = { Text(stringResource(R.string.logout_clear_messages_text)) }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BlocklistDialog(
    blocklist: ImmutableList<UserUi>,
    onAction: (ProfileUiAction) -> Unit,
    onDismiss: () -> Unit
) {
    val imageModifier = Modifier
        .size(30.dp)
        .clip(CircleShape)

    Dialog(onDismissRequest = onDismiss) {
        LazyColumn {
            if (blocklist.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_blocked_users))
                    }
                }
            }
            itemsIndexed(blocklist) { index, user ->
                SegmentedListItem(
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = blocklist.count(),
                    ),
                    onClick = {
                        onAction(ProfileUiAction.UnblockUser(user, onDismiss))
                    },
                    leadingContent = {
                        LoadImage(
                            url = user.profilePictureUrl,
                            modifier = imageModifier
                        )
                    }
                ) {
                    Text(user.displayName)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun DevicesDialog(
    devices: ImmutableList<DeviceUi>,
    onAction: (ProfileUiAction) -> Unit,
    onDismiss: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearMessagesInDeviceDialog by remember { mutableStateOf(false) }
    var deviceToLogout by remember { mutableStateOf<DeviceUi?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Column {
            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = {
                        deviceToLogout = null
                        showLogoutDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showClearMessagesInDeviceDialog = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                deviceToLogout = null
                                showLogoutDialog = false
                            }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    },
                    title = { Text(stringResource(R.string.device_logout_warning_title)) },
                    text = { Text(stringResource(R.string.device_logout_warning_text)) }
                )
            }

            if (showClearMessagesInDeviceDialog) {
                val deviceToLogoutSafe =
                    deviceToLogout ?: throw IllegalStateException("Device to logout is null")

                AlertDialog(
                    onDismissRequest = onDismiss,
                    title = { Text(stringResource(R.string.logout_clear_messages_title)) },
                    text = { Text(stringResource(R.string.logout_clear_messages_text)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onAction(
                                    ProfileUiAction.LogoutDeviceByClearingMessages(
                                        deviceToLogoutSafe
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout_by_clearing_messages))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                onAction(
                                    ProfileUiAction.LogoutDeviceWithoutClearingMessages(
                                        deviceToLogoutSafe
                                    )
                                )
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(stringResource(R.string.logout_without_clearing_messages))
                        }
                    }
                )
            }

            LazyColumn {
                item {
                    Text(
                        text = stringResource(R.string.your_devices),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                itemsIndexed(devices) { index, device ->
                    val isCurrentDevice = device.toDevice() == AppState.currentDevice.collectAsState().value

                    SegmentedListItem(
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index,
                            count = devices.count(),
                        ),
                        onClick = {
                            if (!isCurrentDevice) {
                                deviceToLogout = device
                                showLogoutDialog = true
                            }
                        },
                        leadingContent = {
                            val icon = when (device.type) {
                                Device.Type.Mobile -> Icons.Default.PhoneAndroid
                                Device.Type.Tablet -> Icons.Default.TabletAndroid
                            }
                            Icon(icon, null)
                        }
                    ) {
                        val text = if (isCurrentDevice) "${device.name} (current)" else device.name
                        Text(text)
                    }
                }
            }
        }
    }
}

private val previewContent = @Composable {
    VaultChatTheme {
        AbstractProfileScreen(
            onBack = {},
            user = HomeUiState.defaultUsers.first(),
            devices = HomeUiState.defaultDevices,
            blocklist = HomeUiState.defaultUsers.subList(15, 20),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLightModePreview() {
    previewContent.invoke()
}