package dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.auth.ui.util.AppConstants
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthUiState
import dev.jason.app.compose.vaultchat.theme.MessengerTheme

@Composable
fun ExtraLargeEmailLoginScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterHorizontally)
        ) {

            Text(
                text = stringResource(R.string.email_login),
                style = AppConstants.largeTextStyle
            )

            Spacer(Modifier.height(150.dp))

            Card(
                shape = RoundedCornerShape(AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
            ) {
                LoginEmailForm(
                    uiState = uiState,
                    updateState = updateState,
                    onSignInClick = onSignInClick,
                    arrangementSpace = AppConstants.LARGE_DP_VALUES.arrangementSpace,
                    contentPadding = AppConstants.LARGE_DP_VALUES.emailFormContentPadding,
                    modifier = Modifier.height(AppConstants.LARGE_DP_VALUES.height)
                )
            }
        }
    }
}

@Preview
@Composable
private fun LandscapePhoneEmailLoginScreenLightModePreview() {
    MessengerTheme {
        ExtraLargeEmailLoginScreen(
            uiState = EmailAuthUiState("user@domain.com"),
            updateState = {},
            onSignInClick = {}
        )
    }
}