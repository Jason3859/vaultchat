package dev.jason.app.compose.messenger.auth.ui.screen.email_auth.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.messenger.R
import dev.jason.app.compose.messenger.auth.ui.util.AppConstants
import dev.jason.app.compose.messenger.auth.ui.viewmodel.email_auth.EmailAuthUiState
import dev.jason.app.compose.messenger.theme.MessengerTheme

@Composable
internal fun LargeEmailLoginScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.signin_using_email),
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
                    arrangementSpace = AppConstants.LARGE_DP_VALUES.arrangementSpace,
                    contentPadding = AppConstants.LARGE_DP_VALUES.emailFormContentPadding,
                    modifier = Modifier.height(AppConstants.LARGE_DP_VALUES.height),
                    onSignInClick = onSignInClick
                )
            }
        }
    }
}

@Preview(
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait"
)
@Composable
private fun LargeEmailLoginScreenLightModePreview() {
    MessengerTheme {
        LargeEmailLoginScreen(
            uiState = EmailAuthUiState("email", "password"),
            updateState = {},
            onSignInClick = {}
        )
    }
}