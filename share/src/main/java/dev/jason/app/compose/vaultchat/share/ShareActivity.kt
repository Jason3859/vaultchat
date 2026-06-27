package dev.jason.app.compose.vaultchat.share

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.user.User
import dev.jason.app.compose.vaultchat.core.model.user.UserUi
import dev.jason.app.compose.vaultchat.core.model.user.toUi
import dev.jason.app.compose.vaultchat.core.ui.LoadImage
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme
import dev.jason.app.compose.vaultchat.feature.connections.ConnectionsService
import dev.jason.app.compose.vaultchat.feature.messages.MessageDatabaseService
import dev.jason.app.compose.vaultchat.feature.messaging.MessagingApiService
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime

class ShareActivity : ComponentActivity() {

    private val connectionsService: ConnectionsService by inject()
    private val messagingApiService: MessagingApiService by inject()
    private val messageDatabaseService: MessageDatabaseService by inject()

    private val connections = mutableStateListOf<UserUi>()
    private val selectedUsers = mutableStateListOf<UserUi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Firebase.auth.currentUser == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show()
            finish()
        }

        if (getSharedText() == null) {
            Toast.makeText(this, "Invalid session", Toast.LENGTH_LONG).show()
            finish()
        }

        lifecycleScope.launch {
            connectionsService.getConnections().collect { connections ->
                this@ShareActivity.connections.addAll(connections.map(User::toUi))
            }
        }

        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Share") })
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = ::sendMessage,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null)
                    }
                }
            ) { innerPadding ->
                ShareScreen(
                    connections = connections.toImmutableList(),
                    innerPadding = innerPadding,
                    selectedUsers = selectedUsers.toImmutableList(),
                    onUserItemClick = { user ->
                        selectedUsers.apply {
                            if (contains(user)) {
                                remove(user)
                            } else {
                                add(user)
                            }
                        }
                    }
                )
            }
        }
    }

    private fun sendMessage() {
        lifecycleScope.launch {
            selectedUsers.forEach { user ->
                val uid = Firebase.auth.currentUser?.uid!!
                val message = Message(
                    from = uid,
                    to = user.uid,
                    text = getSharedText()!!,
                    timestamp = LocalDateTime.now()
                )

                messagingApiService.sendMessage(message)
                messageDatabaseService.addMessage(message)
            }
        }.invokeOnCompletion { finish() }

        startActivity(Intent("dev.jason.app.compose.vaultchat.main.ACTION_START_MAIN_ACTIVITY"))
    }

    private fun getSharedText(): String? {
        return if (intent.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(Intent.EXTRA_TEXT)
        } else null
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ShareScreen(
    connections: ImmutableList<UserUi>,
    selectedUsers: ImmutableList<UserUi>,
    onUserItemClick: (UserUi) -> Unit,
    innerPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(contentPadding = innerPadding) {
        items(connections) { user ->
            UserItem(
                selected = user in selectedUsers,
                onClick = { onUserItemClick(user) },
                user = user,
                index = connections.indexOf(user),
                count = connections.count()
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun UserItem(
    selected: Boolean,
    onClick: () -> Unit,
    user: UserUi,
    index: Int,
    count: Int
) {
    SegmentedListItem(
        selected = selected,
        onClick = onClick,
        shapes = ListItemDefaults.segmentedShapes(
            index = index,
            count = count
        ),
        leadingContent = {
            LoadImage(
                url = user.profilePictureUrl
            )
        }
    ) {
        Text(user.displayName)
    }
}

@Preview
@Composable
private fun ShareScreenPreview() {
    VaultChatTheme {
        ShareScreen(
            connections = List(10) { index ->
                UserUi(
                    uid = "user @$index",
                    displayName = "Name @$index",
                    profilePictureUrl = "url",
                    status = User.Status.Online
                )
            }.toImmutableList(),
            selectedUsers = List(10) { index ->
                UserUi(
                    uid = "user @$index",
                    displayName = "Name @$index",
                    profilePictureUrl = "url",
                    status = User.Status.Online
                )
            }.toImmutableList(),
            onUserItemClick = {  }
        )
    }
}
