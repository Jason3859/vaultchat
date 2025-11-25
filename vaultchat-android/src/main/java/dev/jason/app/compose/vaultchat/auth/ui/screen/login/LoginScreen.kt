package dev.jason.app.compose.vaultchat.auth.ui.screen.login

import androidx.compose.runtime.Composable
import dev.jason.app.compose.vaultchat.auth.ui.util.DeviceType

@Composable
fun LoginScreen(
    onSignInUsingGoogleClick: () -> Unit,
    onSigninUsingGitHubClick: () -> Unit,
    onSigninUsingEmailClick: () -> Unit,
    deviceType: DeviceType
) = when (deviceType) {
    is DeviceType.Expanded -> ExtraLargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick,
        onSigninUsingEmailClick
    )

    is DeviceType.ExtraLarge -> ExtraLargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick,
        onSigninUsingEmailClick
    )

    is DeviceType.Foldable -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick,
        onSigninUsingEmailClick
    )

    is DeviceType.Large -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick,
        onSigninUsingEmailClick
    )

    is DeviceType.Medium -> LargeLoginScreen(
        onSignInUsingGoogleClick,
        onSigninUsingGitHubClick,
        onSigninUsingEmailClick
    )

    is DeviceType.Compact -> {
        if (deviceType.isLandscapePhone)
            LandscapePhoneLoginScreen(onSignInUsingGoogleClick, onSigninUsingGitHubClick, onSigninUsingEmailClick)

        else
            CompactLoginScreen(
                onSignInUsingGoogleClick,
                onSigninUsingGitHubClick,
                onSigninUsingEmailClick
            )
    }
}