package dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login

import androidx.compose.runtime.Composable
import dev.jason.app.compose.vaultchat.auth.ui.util.DeviceType
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthUiState

@Composable
fun EmailLoginScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit,
    deviceType: DeviceType
) = when (deviceType) {
    is DeviceType.Expanded -> ExtraLargeEmailLoginScreen(uiState, updateState, onSignInClick)
    is DeviceType.ExtraLarge -> ExtraLargeEmailLoginScreen(uiState, updateState, onSignInClick)
    is DeviceType.Foldable -> LargeEmailLoginScreen(uiState, updateState, onSignInClick)
    is DeviceType.Large -> LargeEmailLoginScreen(uiState, updateState, onSignInClick)
    is DeviceType.Medium -> LargeEmailLoginScreen(uiState, updateState, onSignInClick)
    is DeviceType.Compact -> {
        if (deviceType.isLandscapePhone)
            LandscapePhoneEmailLoginScreen(uiState, updateState, onSignInClick)
        else
            CompactEmailLoginScreen(uiState, updateState, onSignInClick)
    }
}