package dev.jason.app.compose.messenger.auth.ui.screen.login

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.messenger.R
import dev.jason.app.compose.messenger.auth.ui.util.AppConstants
import dev.jason.app.compose.messenger.theme.MessengerTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun CompactLoginScreen(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit,
    onSigninUsingEmailClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val bottomColumnHeightValue = 0.3f

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f - bottomColumnHeightValue)
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        style = AppConstants.compactTextStyle
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(bottomColumnHeightValue)
                    .fillMaxWidth()
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(20.dp),
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(
                        topStart = AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape,
                        topEnd = AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape
                    )
                ) {
                    AuthButtonsGroup(
                        onSignInUsingGoogleClick = onSignInUsingGoogleClick,
                        onSigninUsingGitHubClick = onSigninUsingGitHubClick,
                        onSigninUsingEmailClick = onSigninUsingEmailClick
                    )
                }
            }
        }
    }
}

@Composable
internal fun AuthButtonsGroup(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit,
    onSigninUsingEmailClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = AppConstants.COMPACT_DP_VALUES.height,
    fontSize: TextUnit = AppConstants.COMPACT_DP_VALUES.fontSize,
    iconSize: Dp = AppConstants.COMPACT_DP_VALUES.iconSize,
    arrangementSpace: Dp = AppConstants.COMPACT_DP_VALUES.arrangementSpace
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(arrangementSpace)
    ) {
        AuthButton(
            onClick = onSignInUsingGoogleClick,
            icon = painterResource(R.drawable.google),
            text = stringResource(R.string.signin_using_google),
            buttonHeight = buttonHeight,
            fontSize = fontSize,
            iconSize = iconSize
        )

        AuthButton(
            onClick = onSigninUsingGitHubClick,
            icon = painterResource(R.drawable.github),
            text = stringResource(R.string.signin_using_github),
            buttonHeight = buttonHeight,
            fontSize = fontSize,
            iconSize = iconSize
        )

        AuthButton(
            onClick = onSigninUsingEmailClick,
            icon = painterResource(R.drawable.email),
            text = stringResource(R.string.signin_using_email),
            buttonHeight = buttonHeight,
            fontSize = fontSize,
            iconSize = iconSize
        )
    }
}

@Composable
internal fun AuthButton(
    outlined: Boolean = false,
    onClick: () -> Unit,
    text: String,
    buttonHeight: Dp = AppConstants.COMPACT_DP_VALUES.height,
    fontSize: TextUnit = AppConstants.COMPACT_DP_VALUES.fontSize,
    iconSize: Dp = AppConstants.COMPACT_DP_VALUES.iconSize,
    icon: Painter? = null,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier
) {
    Button(
        outlined = outlined,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .then(modifier)
    ) {
        Box {
            icon?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = text,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(iconSize)
            ) {
                Text(
                    text = text,
                    fontSize = fontSize
                )
            }
        }
    }
}

@Composable
private fun Button(
    outlined: Boolean = false,
    @SuppressLint("ModifierParameter")
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            content = content,
            modifier = modifier
        )
    } else {
        Button(
            onClick = onClick,
            content = content,
            modifier = modifier
        )
    }
}

@Composable
private fun CompactLoginScreen() {
    CompactLoginScreen(
        onSignInUsingGoogleClick = {},
        onSigninUsingEmailClick = {},
        onSigninUsingGitHubClick = {}
    )
}

@Preview(
    device = "id:pixel_9a",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun MobileLoginScreenLightModePreview() {
    MessengerTheme {
        CompactLoginScreen()
    }
}

@Preview(
    device = "id:pixel_9a",
    wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun MobileLoginScreenDarkModePreview() {
    MessengerTheme {
        CompactLoginScreen()
    }
}