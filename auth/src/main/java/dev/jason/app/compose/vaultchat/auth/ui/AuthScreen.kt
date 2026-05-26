package dev.jason.app.compose.vaultchat.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.jason.app.compose.vaultchat.core.ui.DeviceType
import dev.jason.app.compose.vaultchat.core.ui.theme.VaultChatTheme

@Composable
fun AuthScreen(
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val deviceType = DeviceType.rememberWindowSize()

    when (deviceType) {
        is DeviceType.Compact -> CompactAuthScreen(onSignInClick, modifier)
        is DeviceType.Medium -> MediumAuthScreen(onSignInClick, modifier)
        is DeviceType.Foldable -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.Expanded -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.Large -> LargeAuthScreen(onSignInClick, modifier)
        is DeviceType.ExtraLarge -> LargeAuthScreen(onSignInClick, modifier)
    }
}

@PreviewScreenSizes
@Composable
private fun AuthScreenPreview() {
    VaultChatTheme {
        AuthScreen(
            onSignInClick = {}
        )
    }
}