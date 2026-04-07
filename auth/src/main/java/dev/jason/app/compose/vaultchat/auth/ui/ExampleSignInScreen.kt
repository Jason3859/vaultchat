package dev.jason.app.compose.vaultchat.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.jason.app.compose.vaultchat.auth.R

@Composable
fun ExampleSignInScreen(onSignInClick: () -> Unit, modifier: Modifier = Modifier) {
    // Example SignIn Screen
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onSignInClick
        ) {
            Text(stringResource(R.string.sign_in_with_google))
        }
    }
}