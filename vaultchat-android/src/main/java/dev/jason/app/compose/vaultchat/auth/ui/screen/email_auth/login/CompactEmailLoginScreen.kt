package dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.AuthButton
import dev.jason.app.compose.vaultchat.auth.ui.util.AppConstants
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthUiState
import dev.jason.app.compose.vaultchat.theme.MessengerTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun CompactEmailLoginScreen(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val bottomColumnHeightValue = 0.35f

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1f - bottomColumnHeightValue)
                ) {
                    Text(
                        text = stringResource(R.string.signin_using_email),
                        style = AppConstants.compactTextStyle
                    )
                }
            }

            Box {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(bottomColumnHeightValue)
                        .fillMaxWidth()
                ) {
                    Card(
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                        elevation = CardDefaults.cardElevation(20.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LoginEmailForm(uiState, updateState, onSignInClick)
                    }
                }
            }
        }
    }
}

@Composable
internal fun LoginEmailForm(
    uiState: EmailAuthUiState,
    updateState: (EmailAuthUiState) -> Unit,
    onSignInClick: () -> Unit,
    arrangementSpace: Dp = AppConstants.COMPACT_DP_VALUES.arrangementSpace,
    contentPadding: Dp = AppConstants.COMPACT_DP_VALUES.emailFormContentPadding,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(arrangementSpace)
    ) {
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { updateState(uiState.copy(email = it)) },
            placeholder = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
            singleLine = true,
            shape = RoundedCornerShape(AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape),
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        )

        val visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation()

        OutlinedTextField(
            value = uiState.password,
            onValueChange = { updateState(uiState.copy(password = it)) },
            placeholder = { Text(stringResource(R.string.password)) },
            singleLine = true,
            visualTransformation = visualTransformation,
            shape = RoundedCornerShape(AppConstants.STATIC_APP_DP_VALUES.roundedCornerShape),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSignInClick() }),
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier),
            trailingIcon = {
                IconButton(
                    onClick = {
                        updateState(uiState.copy(showPassword = !uiState.showPassword))
                    }
                ) {
                    Icon(
                        painter = painterResource(if (uiState.showPassword) R.drawable.ic_visibility_off else R.drawable.ic_visibility_on),
                        contentDescription = null
                    )
                }
            }
        )

        AuthButton(
            onClick = onSignInClick,
            text = stringResource(R.string.login)
        )
    }
}

@Preview
@Composable
private fun CompactEmailLoginScreenLightModePreview() {
    MessengerTheme {
        CompactEmailLoginScreen(
            uiState = EmailAuthUiState("username@domain.com", "password"),
            updateState = {},
            onSignInClick = {}
        )
    }
}