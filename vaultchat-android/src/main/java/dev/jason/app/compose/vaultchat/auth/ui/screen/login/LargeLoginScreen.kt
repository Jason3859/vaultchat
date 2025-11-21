package dev.jason.app.compose.vaultchat.auth.ui.screen.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.auth.ui.util.AppConstants
import dev.jason.app.compose.vaultchat.theme.MessengerTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun LargeLoginScreen(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit,
    onSigninUsingEmailClick: () -> Unit
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
                text = stringResource(R.string.welcome),
                style = AppConstants.largeTextStyle
            )

            Spacer(Modifier.height(150.dp))

            Card(
                shape = RoundedCornerShape(AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape),
                modifier = Modifier
                    .fillMaxWidth(0.65f)
            ) {
                AuthButtonsGroup(
                    onSignInUsingGoogleClick = onSignInUsingGoogleClick,
                    onSigninUsingGitHubClick = onSigninUsingGitHubClick,
                    onSigninUsingEmailClick = onSigninUsingEmailClick,
                    buttonHeight = AppConstants.LARGE_DP_VALUES.height,
                    fontSize = AppConstants.LARGE_DP_VALUES.fontSize,
                    iconSize = AppConstants.LARGE_DP_VALUES.iconSize,
                    arrangementSpace = AppConstants.LARGE_DP_VALUES.arrangementSpace,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
private fun LargeLoginScreen() {
    LargeLoginScreen(
        onSigninUsingEmailClick = {},
        onSigninUsingGitHubClick = {},
        onSignInUsingGoogleClick = {}
    )
}

@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TabletLoginScreenLightModePreview() {
    MessengerTheme {
        LargeLoginScreen()
    }
}

@Preview(
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun TabletLoginScreenDarkModePreview() {
    MessengerTheme {
        LargeLoginScreen()
    }
}