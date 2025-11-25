package dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin

import androidx.compose.runtime.Composable
import dev.jason.app.compose.vaultchat.auth.ui.util.DeviceType
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthUiState

@Composable
fun EmailSigninScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit,
    onLoginClick: () -> Unit,
    deviceType: DeviceType
) = when (deviceType) {
    is DeviceType.Expanded -> ExtraLargeEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    is DeviceType.ExtraLarge -> ExtraLargeEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    is DeviceType.Foldable -> LargeEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    is DeviceType.Large -> LargeEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    is DeviceType.Medium -> LargeEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    is DeviceType.Compact -> {
        if (deviceType.isLandscapePhone)
            LandscapePhoneEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
        else
            CompactEmailSigninScreen(uiState, updateState, onSignInClick, onLoginClick)
    }
}