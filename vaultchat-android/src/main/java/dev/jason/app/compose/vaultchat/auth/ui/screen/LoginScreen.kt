package dev.jason.app.compose.vaultchat.auth.ui.screen

import androidx.compose.runtime.Composable
import dev.jason.app.compose.vaultchat.core.util.DeviceType

@Composable
fun LoginScreen(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit,
    deviceType: DeviceType
) = when (deviceType) {

    is DeviceType.Expanded -> {
        if (deviceType.isLandscapePhone)
            LandscapePhoneLoginScreen(
                onSignInUsingGoogleClick,
                onSigninUsingGitHubClick
            )
        else
            ExtraLargeLoginScreen(
                onSignInUsingGoogleClick,
                onSigninUsingGitHubClick
            )
    }

    is DeviceType.ExtraLarge -> ExtraLargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick
    )

    is DeviceType.Foldable -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick
    )

    is DeviceType.Large -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick
    )

    is DeviceType.Medium -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick
    )

    is DeviceType.Compact -> CompactLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick
    )
}