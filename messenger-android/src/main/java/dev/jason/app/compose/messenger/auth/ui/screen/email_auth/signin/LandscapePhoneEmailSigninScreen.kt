package dev.jason.app.compose.messenger.auth.ui.screen.email_auth.signin

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
import dev.jason.app.compose.messenger.R
import dev.jason.app.compose.messenger.auth.ui.util.AppConstants
import dev.jason.app.compose.messenger.auth.ui.viewmodel.email_auth.EmailAuthUiState
import dev.jason.app.compose.messenger.theme.MessengerTheme

@Composable
fun LandscapePhoneEmailSigninScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit,
    onLoginClick: () -> Unit
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
                text = stringResource(R.string.signin_using_email),
                style = AppConstants.compactTextStyle
            )

            Card {
                SigninEmailForm(uiState, updateState, onSignInClick, onLoginClick)
            }
        }
    }
}

@Preview
@Composable
private fun LandscapePhoneEmailSigninScreenLightModePreview() {
    MessengerTheme {
        LandscapePhoneEmailSigninScreen(
            uiState = EmailAuthUiState("user@domain.com"),
            updateState = {},
            onSignInClick = {},
            onLoginClick = {}
        )
    }
}