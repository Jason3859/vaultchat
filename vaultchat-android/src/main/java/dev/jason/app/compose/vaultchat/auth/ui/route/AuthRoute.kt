package dev.jason.app.compose.vaultchat.auth.ui.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed interface AuthRoute : NavKey {

    @Serializable data object LoginScreen : AuthRoute
    @Serializable data object EmailSigninScreen : AuthRoute
    @Serializable data object EmailLoginScreen : AuthRoute
}