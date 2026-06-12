package dev.jason.app.compose.vaultchat.messaging.ui.profile

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Block
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.messaging.ui.R
import dev.jason.app.compose.vaultchat.messaging.ui.home.HomeUiState
import org.koin.androidx.compose.koinViewModel

enum class ProfileScreenRowItemId {
    Devices, Blocklist, Logout
}

@Composable
fun ProfileScreen(
    showCurrentUserProfile: Boolean,
    onBack: () -> Unit
) {
    val viewModel: ProfileScreenViewModel = koinViewModel()

    val devices by viewModel.devices.collectAsStateWithLifecycle()
    val blocklist by viewModel.blocklist.collectAsStateWithLifecycle()
    val currentUser by AppState.currentUser.collectAsStateWithLifecycle()
    val otherUser by AppState.otherUser.collectAsStateWithLifecycle()

    val onLogoutClick: (Boolean) -> Unit = { clearMessages ->
        viewModel.logoutCurrentDevice(
            clearMessages = clearMessages,
            onSuccess = { /*TODO*/ }
        )
    }
    val onDeviceLogoutClick: (Device, Boolean) -> Unit = { device, clearMessages ->
        viewModel.logoutDevice(
            device = device,
            clearMessages = clearMessages,
            onSuccess = { /*nothing to do here*/ }
        )
    }

    BackHandler { onBack.invoke() }

    if (showCurrentUserProfile)
        ProfileScreen(
            onBack = onBack,
            user = currentUser ?: throw IllegalStateException("user is null"),
            devices = devices,
            blocklist = blocklist,
            onLogoutWithoutClearingMessagesClick = { onLogoutClick(false) },
            onLogoutClearingMessagesClick = { onLogoutClick(true) },
            onDeviceLogoutClick = onDeviceLogoutClick,
        )
    else
        ProfileScreen(
            onBack = onBack,
            user = otherUser ?: throw IllegalStateException("other user is null")
        )
}

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    user: User,
    devices: List<Device>? = null,
    blocklist: List<User>? = null,
    onLogoutWithoutClearingMessagesClick: (() -> Unit)? = null,
    onLogoutClearingMessagesClick: (() -> Unit)? = null,
    onDeviceLogoutClick: ((Device, Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    val imageModifier = Modifier
        .size(175.dp)
        .clip(CircleShape)

    var currentDialog by remember { mutableStateOf<ProfileScreenRowItemId?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
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
    ) { innerPadding ->
        when (currentDialog) {
            ProfileScreenRowItemId.Devices -> DevicesDialog(
                // it should not throw any exception here because
                // it will only be triggered when buttons below are visible
                // but those buttons are hidden when these values (i.e., devices and blocklist) are null
                devices = devices!!,
                onDismiss = { currentDialog = null },
                onLogoutDeviceClick = { d, b -> onDeviceLogoutClick?.invoke(d, b) }
            )

            ProfileScreenRowItemId.Blocklist -> BlocklistDialog(
                blocklist = blocklist!!,
                onDismiss = { currentDialog = null }
            )

            ProfileScreenRowItemId.Logout -> LogoutDialog(
                onDismiss = { currentDialog = null },
                onLogoutClearingMessagesClick = { onLogoutClearingMessagesClick?.invoke() },
                onLogoutWithoutClearingMessagesClick = { onLogoutWithoutClearingMessagesClick?.invoke() }
            )

            null -> {}
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user.profilePictureUrl)
                            .crossfade(true)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        contentDescription = null,
                        modifier = imageModifier,
                        error = {
                            Log.e(
                                "ProfileScreen",
                                "ProfileScreen: error while loading image",
                                it.result.throwable
                            )

                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                modifier = imageModifier
                            )
                        }
                    )

                    Text(
                        text = user.displayName,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.labelMediumEmphasized
                    )
                }
            }

            if (devices != null && blocklist != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mapOf(
                        ProfileScreenRowItemId.Devices to Icons.Default.Devices,
                        ProfileScreenRowItemId.Blocklist to Icons.Default.Block,
                        ProfileScreenRowItemId.Logout to Icons.AutoMirrored.Filled.Logout
                    ).forEach { (id, icon) ->
                        OutlinedButton(
                            onClick = { currentDialog = id },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(icon, null)
                                Text(id.name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogoutDialog(
    onDismiss: () -> Unit,
    onLogoutClearingMessagesClick: () -> Unit,
    onLogoutWithoutClearingMessagesClick: () -> Unit
) {
    var showAnotherDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.logout_warning_title))
        },
        confirmButton = {
            TextButton(
                onClick = { showAnotherDialog = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(stringResource(R.string.logout))
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

    if (showAnotherDialog) {
        AlertDialog(
            onDismissRequest = {
                showAnotherDialog = false // ignore warning. it just works.
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = onLogoutClearingMessagesClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_by_clearing_messages))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onLogoutWithoutClearingMessagesClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.logout_without_clearing_messages))
                }
            },
            title = {
                Text(stringResource(R.string.logout_clear_messages_title))
            },
            text = {
                Text(stringResource(R.string.logout_clear_messages_text))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BlocklistDialog(
    blocklist: List<User>,
    onDismiss: () -> Unit
) {
    val imageModifier = Modifier
        .size(30.dp)
        .clip(CircleShape)

    Dialog(
        onDismissRequest = onDismiss
    ) {
        LazyColumn {
            if (blocklist.isEmpty()) {
                item {
                    Box(
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
                    onClick = {},
                    leadingContent = {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.profilePictureUrl)
                                .crossfade(true)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .build(),
                            contentDescription = null,
                            modifier = imageModifier,
                            error = {
                                Log.e(
                                    "ProfileScreen",
                                    "BlocklistDialog: error while loading image",
                                    it.result.throwable
                                )

                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = imageModifier
                                )
                            }
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
    devices: List<Device>,
    onLogoutDeviceClick: (device: Device, clearMessages: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showClearMessagesInDeviceDialog by remember { mutableStateOf(false) }
    var deviceToLogout by remember { mutableStateOf<Device?>(null) }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = {
                    deviceToLogout = null
                    showLogoutDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showClearMessagesInDeviceDialog = true
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.logout))
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = {
                            deviceToLogout = null
                            showLogoutDialog = false
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                title = {
                    Text(stringResource(R.string.device_logout_warning_title))
                },
                text = {
                    Text(stringResource(R.string.device_logout_warning_text))
                }
            )
        }

        if (showClearMessagesInDeviceDialog) {
            val deviceToLogout =
                deviceToLogout ?: throw IllegalStateException("device to logout is null")

            AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(stringResource(R.string.logout_clear_messages_title)) },
                text = { Text(stringResource(R.string.logout_clear_messages_text)) },
                confirmButton = {
                    TextButton(
                        onClick = { onLogoutDeviceClick(deviceToLogout, true) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.logout_by_clearing_messages))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { onLogoutDeviceClick(deviceToLogout, false) },
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
                Text(stringResource(R.string.your_devices))
            }
            itemsIndexed(devices) { index, device ->
                SegmentedListItem(
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = devices.count(),
                    ),
                    onClick = {
                        deviceToLogout = device
                        showLogoutDialog = true
                    },
                    leadingContent = {
                        val icon = when (device.type) {
                            Device.Type.Mobile -> Icons.Default.PhoneAndroid
                            Device.Type.Tablet -> Icons.Default.TabletAndroid
                        }

                        Icon(icon, null)
                    }
                ) {
                    val text =
                        if (device == AppState.currentDevice.collectAsState().value)
                            "${device.name} (current)"
                        else device.name

                    Text(text)
                }
            }
        }
    }
}

val previewContent = @Composable {
    VaultChatTheme {
        ProfileScreen(
            onBack = {},
            user = HomeUiState.defaultUsers.first(),
            devices = HomeUiState.defaultDevices,
            blocklist = HomeUiState.defaultUsers.subList(15, 20),
            onLogoutWithoutClearingMessagesClick = { },
            onLogoutClearingMessagesClick = { },
        )
    }
}

@Preview
@Composable
private fun ProfileScreenLightModePreview() {
    previewContent.invoke()
}