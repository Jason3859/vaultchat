package dev.jason.app.compose.vaultchat.web_socket.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme

@Composable
fun DetailScreenPlaceHolder(modifier: Modifier = Modifier) {
    MessengerTheme {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.placeholder_text))
        }
    }
}