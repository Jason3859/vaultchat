package dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.auth.ui.util.AppConstants
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthUiState
import dev.jason.app.compose.vaultchat.theme.MessengerTheme

@Composable
fun LandscapePhoneEmailLoginScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = stringResource(R.string.email_login),
                style = AppConstants.compactTextStyle
            )

            Card {
                LoginEmailForm(uiState, updateState, onSignInClick)
            }
        }
    }
}

@Preview
@Composable
private fun LandscapePhoneEmailLoginScreenLightModePreview() {
    MessengerTheme {
        LandscapePhoneEmailLoginScreen(
            uiState = EmailAuthUiState("user@domain.com"),
            updateState = {},
            onSignInClick = {}
        )
    }
}