package dev.jason.app.compose.vaultchat.auth.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.MessengerActivity
import dev.jason.app.compose.vaultchat.auth.data.google.FirebaseGoogleAuthentication
import dev.jason.app.compose.vaultchat.auth.ui.route.AuthRoute
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login.CompactEmailLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login.ExtraLargeEmailLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login.LandscapePhoneEmailLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login.LargeEmailLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin.CompactEmailSigninScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin.ExtraLargeEmailSigninScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin.LandscapePhoneEmailSigninScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin.LargeEmailSigninScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.CompactLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.ExtraLargeLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.LandscapePhoneLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.LargeLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.util.DeviceType
import dev.jason.app.compose.vaultchat.auth.ui.util.SnackbarController
import dev.jason.app.compose.vaultchat.auth.ui.util.getDeviceType
import dev.jason.app.compose.vaultchat.auth.ui.util.isLandscapePhone
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.github_auth.GitHubAuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.google_auth.GoogleAuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessengerActivity.AuthNavGraph(onDone: () -> Unit) {

    val backStack = rememberNavBackStack(AuthRoute.LoginScreen.MainLoginScreen)

    val snackbarHostState = remember { SnackbarHostState() }

    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val deviceType = getDeviceType(windowAdaptiveInfo)

    val googleAuthViewModel: GoogleAuthViewModel = koinViewModel()
    val gitHubAuthViewModel: GitHubAuthViewModel = koinViewModel()
    val emailAuthViewModel: EmailAuthViewModel = koinViewModel()

    val emailAuthUiState by emailAuthViewModel.uiState.collectAsState()

    val googleSignin: () -> Unit = {
        lifecycleScope.launch {
            FirebaseGoogleAuthentication.launchCredentialManagerBottomSheet(this@AuthNavGraph) { credential ->
                googleAuthViewModel.signin(credential)
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            onDone()
                        } else {
                            SnackbarController.sendEvent(it.exception?.message!!)
                            Log.e(
                                "AuthNavGraph",
                                "AuthNavGraph: exception while logging in using google",
                                it.exception
                            )
                        }
                    }
            }
        }
    }
    val githubSignin: () -> Unit = {
        gitHubAuthViewModel.signin(this)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onDone()
                } else {
                    SnackbarController.sendEvent(it.exception?.message!!)
                    Log.e(
                        "AuthNavGraph",
                        "AuthNavGraph: exception while logging in using github",
                        it.exception
                    )
                }
            }
    }
    val emailSignin: () -> Unit = { backStack.add(AuthRoute.EmailSigninScreen.MainEmailSigninScreen) }
    val emailLogin: () -> Unit = { backStack.add(AuthRoute.EmailLoginScreen.MainEmailLoginScreen) }
    val onEmailSignInClick: () -> Unit = {
        emailAuthViewModel.signin()
            ?.addOnCompleteListener {
                if (!it.isSuccessful) {
                    SnackbarController.sendEvent(it.exception?.message!!)
                    Log.e(
                        "AuthNavGraph",
                        "AuthNavGraph: exception while signing in using email",
                        it.exception
                    )
                } else {
                    onDone()
                }
            }
    }

    val onEmailLoginClick: () -> Unit = {
        emailAuthViewModel.login()
            ?.addOnCompleteListener {
                if (!it.isSuccessful) {
                    SnackbarController.sendEvent(it.exception?.message!!)
                    Log.e(
                        "AuthNavGraph",
                        "AuthNavGraph: exception while logging in using email",
                        it.exception
                    )
                } else {
                    onDone()
                }
            }
    }

    LaunchedEffect(backStack) {
        for (route in backStack) {
            Log.d(
                "AuthNavGraph",
                "------------------------------------------------------------------------"
            )
            Log.d("AuthNavGraph", "current backstack:")
            Log.d("AuthNavGraph", "\t$route")
            Log.d(
                "AuthNavGraph",
                "------------------------------------------------------------------------"
            )
        }
    }

    LaunchedEffect(windowAdaptiveInfo) {
        Log.d("AuthNavGraph", "Current Device Type: $deviceType")

        when (backStack.last()) {
            is AuthRoute.LoginScreen -> {
                backStack.add(AuthRoute.LoginScreen.MainLoginScreen)
                backStack.removeAt(backStack.lastIndex - 1)
            }

            is AuthRoute.EmailSigninScreen -> {
                backStack.add(AuthRoute.EmailSigninScreen.MainEmailSigninScreen)
                backStack.removeAt(backStack.lastIndex - 1)
            }

            is AuthRoute.EmailLoginScreen -> {
                backStack.add(AuthRoute.EmailLoginScreen.MainEmailLoginScreen)
                backStack.removeAt(backStack.lastIndex - 1)
            }
        }
    }

    LaunchedEffect(true) {
        SnackbarController.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            )
        ) { key ->
            when (key) {
                is AuthRoute.LoginScreen.MainLoginScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        backStack.add(
                            when (deviceType) {
                                is DeviceType.Compact -> AuthRoute.LoginScreen.CompactLoginScreen
                                is DeviceType.ExtraLarge -> AuthRoute.LoginScreen.ExtraLargeLoginScreen
                                is DeviceType.Large -> AuthRoute.LoginScreen.LargeLoginScreen
                                is DeviceType.Medium -> AuthRoute.LoginScreen.LargeLoginScreen
                                is DeviceType.Foldable -> {
                                    if (deviceType.isTabletop)
                                        AuthRoute.LoginScreen.LargeLoginScreen
                                    else
                                        AuthRoute.LoginScreen.CompactLoginScreen
                                }

                                is DeviceType.Expanded -> {
                                    if (deviceType.isLandscapePhone)
                                        AuthRoute.LoginScreen.LandscapePhoneLoginScreen
                                    else
                                        AuthRoute.LoginScreen.LargeLoginScreen
                                }
                            }
                        )

                        backStack.removeAt(backStack.lastIndex - 1)
                    }
                }

                AuthRoute.LoginScreen.CompactLoginScreen -> {
                    NavEntry(key) {
                        CompactLoginScreen(
                            onSignInUsingGoogleClick = googleSignin,
                            onSigninUsingGitHubClick = githubSignin,
                            onSigninUsingEmailClick = emailSignin
                        )
                    }
                }

                is AuthRoute.LoginScreen.LandscapePhoneLoginScreen -> {
                    NavEntry(key) {
                        LandscapePhoneLoginScreen(
                            onSignInUsingGoogleClick = googleSignin,
                            onSigninUsingGitHubClick = githubSignin,
                            onSigninUsingEmailClick = emailSignin
                        )
                    }
                }

                is AuthRoute.LoginScreen.ExtraLargeLoginScreen -> {
                    NavEntry(key) {
                        ExtraLargeLoginScreen(
                            onSignInUsingGoogleClick = googleSignin,
                            onSigninUsingGitHubClick = githubSignin,
                            onSigninUsingEmailClick = emailSignin
                        )
                    }
                }

                is AuthRoute.LoginScreen.LargeLoginScreen -> {
                    NavEntry(key) {
                        LargeLoginScreen(
                            onSignInUsingGoogleClick = googleSignin,
                            onSigninUsingGitHubClick = githubSignin,
                            onSigninUsingEmailClick = emailSignin
                        )
                    }
                }

                is AuthRoute.EmailSigninScreen.MainEmailSigninScreen -> {
                    NavEntry(key) {
                        backStack.add(
                            when (deviceType) {
                                is DeviceType.Compact -> AuthRoute.EmailSigninScreen.CompactEmailSigninScreen
                                is DeviceType.ExtraLarge -> AuthRoute.EmailSigninScreen.ExtraLargeEmailSigninScreen
                                is DeviceType.Large -> AuthRoute.EmailSigninScreen.LargeEmailSigninScreen
                                is DeviceType.Medium -> AuthRoute.EmailSigninScreen.LargeEmailSigninScreen
                                is DeviceType.Foldable -> AuthRoute.EmailSigninScreen.LargeEmailSigninScreen
                                is DeviceType.Expanded -> {
                                    if (deviceType.isLandscapePhone) {
                                        AuthRoute.EmailSigninScreen.LandscapePhoneEmailSigninScreen
                                    } else {
                                        AuthRoute.EmailSigninScreen.LargeEmailSigninScreen
                                    }
                                }
                            }
                        )

                        backStack.removeAt(backStack.lastIndex - 1)
                    }
                }

                is AuthRoute.EmailSigninScreen.CompactEmailSigninScreen -> {
                    NavEntry(key) {
                        CompactEmailSigninScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailSignInClick,
                            onLoginClick = emailLogin
                        )
                    }
                }

                is AuthRoute.EmailSigninScreen.LargeEmailSigninScreen -> {
                    NavEntry(key) {
                        LargeEmailSigninScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailSignInClick,
                            onLoginClick = emailLogin
                        )
                    }
                }

                is AuthRoute.EmailSigninScreen.ExtraLargeEmailSigninScreen -> {
                    NavEntry(key) {
                        ExtraLargeEmailSigninScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailSignInClick,
                            onLoginClick = emailLogin
                        )
                    }
                }

                is AuthRoute.EmailSigninScreen.LandscapePhoneEmailSigninScreen -> {
                    NavEntry(key) {
                        LandscapePhoneEmailSigninScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailSignInClick,
                            onLoginClick = emailLogin
                        )
                    }
                }

                is AuthRoute.EmailLoginScreen.MainEmailLoginScreen -> {
                    NavEntry(key) {
                        backStack.add(
                            when (deviceType) {
                                is DeviceType.Compact -> AuthRoute.EmailLoginScreen.CompactEmailLoginScreen
                                is DeviceType.ExtraLarge -> AuthRoute.EmailLoginScreen.ExtraLargeEmailLoginScreen
                                is DeviceType.Large -> AuthRoute.EmailLoginScreen.LargeEmailLoginScreen
                                is DeviceType.Medium -> AuthRoute.EmailLoginScreen.LargeEmailLoginScreen
                                is DeviceType.Foldable -> AuthRoute.EmailLoginScreen.LargeEmailLoginScreen
                                is DeviceType.Expanded -> {
                                    if (deviceType.isLandscapePhone) {
                                        AuthRoute.EmailLoginScreen.LandscapePhoneEmailLoginScreen
                                    } else {
                                        AuthRoute.EmailLoginScreen.LargeEmailLoginScreen
                                    }
                                }
                            }
                        )

                        backStack.removeAt(backStack.lastIndex - 1)
                    }
                }

                is AuthRoute.EmailLoginScreen.CompactEmailLoginScreen -> {
                    NavEntry(key) {
                        CompactEmailLoginScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailLoginClick
                        )
                    }
                }

                is AuthRoute.EmailLoginScreen.LargeEmailLoginScreen -> {
                    NavEntry(key) {
                        LargeEmailLoginScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailLoginClick
                        )
                    }
                }

                is AuthRoute.EmailLoginScreen.ExtraLargeEmailLoginScreen -> {
                    NavEntry(key) {
                        ExtraLargeEmailLoginScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailLoginClick
                        )
                    }
                }

                is AuthRoute.EmailLoginScreen.LandscapePhoneEmailLoginScreen -> {
                    NavEntry(key) {
                        LandscapePhoneEmailLoginScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailLoginClick
                        )
                    }
                }

                else -> throw IllegalStateException()
            }
        }
    }
}