package dev.jason.app.compose.vaultchat.auth.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LandscapePhoneLoginScreen(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit
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
                text = stringResource(R.string.welcome),
                style = AppConstants.compactTextStyle
            )

            Card(
                shape = RoundedCornerShape(AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
            ) {
                AuthButtonsGroup(
                    onSignInUsingGoogleClick = onSignInUsingGoogleClick,
                    onSigninUsingGitHubClick = onSigninUsingGitHubClick,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun LandscapePhoneLoginScreenLightModePreview() {
    MessengerTheme {
        LandscapePhoneLoginScreen({}, {})
    }
}