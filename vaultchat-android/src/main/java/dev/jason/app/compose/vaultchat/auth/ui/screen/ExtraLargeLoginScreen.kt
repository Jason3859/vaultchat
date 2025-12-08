package dev.jason.app.compose.vaultchat.auth.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import dev.jason.app.compose.vaultchat.core.theme.MessengerTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ExtraLargeLoginScreen(
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
                style = AppConstants.extraLargeTextStyle
            )

            Card(
                shape = RoundedCornerShape(40.dp),
                modifier = Modifier
                    .wrapContentSize()
            ) {
                AuthButtonsGroup(
                    onSignInUsingGoogleClick = onSignInUsingGoogleClick,
                    onSigninUsingGitHubClick = onSigninUsingGitHubClick,
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
private fun ExtraLargeLoginScreen() {
    ExtraLargeLoginScreen(
        onSignInUsingGoogleClick = {},
        onSigninUsingGitHubClick = {}
    )
}

@Preview(
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL, device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
private fun ExtraLargeLoginScreenLightModePreview() {
    MessengerTheme {
        ExtraLargeLoginScreen()
    }
}

@Preview(
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    device = "spec:width=1280dp,height=800dp,dpi=240",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun ExtraLargeLoginScreenDarkModePreview() {
    MessengerTheme {
        ExtraLargeLoginScreen()
    }
}