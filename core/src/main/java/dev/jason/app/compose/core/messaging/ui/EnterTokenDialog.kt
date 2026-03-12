package dev.jason.app.compose.core.messaging.ui

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun EnterTokenDialog(
    token: String,
    onAction: (AppViewModel.Action) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboard.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = token,
                onValueChange = { onAction(AppViewModel.Action.UpdateRemoteToken(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Remote user token") },
                maxLines = 1
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            val localToken = FirebaseMessaging.getInstance().token.await()
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(localToken, localToken)))
                            Toast.makeText(context, "Token copied!", Toast.LENGTH_LONG).show()
                        }
                    }
                ) {
                    Text("Copy token")
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = { onAction(AppViewModel.Action.SubmitRemoteToken) }
                ) {
                    Text("Submit")
                }
            }
        }
    }
}