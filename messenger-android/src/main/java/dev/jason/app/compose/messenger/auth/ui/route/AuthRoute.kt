package dev.jason.app.compose.messenger.auth.ui.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

internal sealed interface AuthRoute : NavKey {

    sealed interface LoginScreen : AuthRoute {
        @Serializable data object MainLoginScreen : AuthRoute
        @Serializable data object CompactLoginScreen : AuthRoute
        @Serializable data object LandscapePhoneLoginScreen : AuthRoute
        @Serializable data object LargeLoginScreen : AuthRoute
        @Serializable data object ExtraLargeLoginScreen : AuthRoute
    }

    sealed interface EmailSigninScreen : AuthRoute {
        @Serializable data object MainEmailSigninScreen : AuthRoute
        @Serializable data object CompactEmailSigninScreen : AuthRoute
        @Serializable data object LandscapePhoneEmailSigninScreen : AuthRoute
        @Serializable data object LargeEmailSigninScreen : AuthRoute
        @Serializable data object ExtraLargeEmailSigninScreen : AuthRoute
    }

    sealed interface EmailLoginScreen : AuthRoute {
        @Serializable data object MainEmailLoginScreen : AuthRoute
        @Serializable data object CompactEmailLoginScreen : AuthRoute
        @Serializable data object LandscapePhoneEmailLoginScreen : AuthRoute
        @Serializable data object LargeEmailLoginScreen : AuthRoute
        @Serializable data object ExtraLargeEmailLoginScreen : AuthRoute
    }
}