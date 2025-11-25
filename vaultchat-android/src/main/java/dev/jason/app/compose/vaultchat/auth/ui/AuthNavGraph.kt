package dev.jason.app.compose.vaultchat.auth.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.computeWindowSizeClass
import dev.jason.app.compose.vaultchat.MessengerActivity
import dev.jason.app.compose.vaultchat.auth.data.google.FirebaseGoogleAuthentication
import dev.jason.app.compose.vaultchat.auth.ui.route.AuthRoute
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.login.EmailLoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.email_auth.signin.EmailSigninScreen
import dev.jason.app.compose.vaultchat.auth.ui.screen.login.LoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.util.SnackbarController
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.email_auth.EmailAuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.github_auth.GitHubAuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.google_auth.GoogleAuthViewModel
import dev.jason.app.compose.vaultchat.util.DeviceType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessengerActivity.AuthNavGraph(onDone: () -> Unit) {

    val backStack = rememberNavBackStack(AuthRoute.LoginScreen)

    val snackbarHostState = remember { SnackbarHostState() }

    val deviceType = rememberWindowSize()

    val googleAuthViewModel: GoogleAuthViewModel = koinViewModel()
    val gitHubAuthViewModel: GitHubAuthViewModel = koinViewModel()
    val emailAuthViewModel: EmailAuthViewModel = koinViewModel()

    val emailAuthUiState by emailAuthViewModel.uiState.collectAsState()

    val focusManager = LocalFocusManager.current

    val googleSignin: () -> Unit = signInWithGoogle(googleAuthViewModel, onDone)
    val githubSignin: () -> Unit = signInWithGitHub(gitHubAuthViewModel, onDone)
    val onEmailSignInClick: () -> Unit = signInWithEmail(focusManager, emailAuthViewModel, onDone)
    val onEmailLoginClick: () -> Unit = loginWithEmail(focusManager, emailAuthViewModel, onDone)

    LaunchedEffect(deviceType) {
        Log.d("AuthNavGraph", "Current Device Type: $deviceType")
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
                is AuthRoute.LoginScreen -> {
                    NavEntry(key) {
                        LoginScreen(
                            onSignInUsingGoogleClick = googleSignin,
                            onSigninUsingGitHubClick = githubSignin,
                            deviceType = deviceType,
                            onSigninUsingEmailClick = { backStack.add(AuthRoute.EmailSigninScreen) }
                        )
                    }
                }

                is AuthRoute.EmailSigninScreen -> {
                    NavEntry(key) {
                        EmailSigninScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailSignInClick,
                            onLoginClick = { backStack.add(AuthRoute.EmailLoginScreen) },
                            deviceType = deviceType
                        )
                    }
                }

                is AuthRoute.EmailLoginScreen -> {
                    NavEntry(key) {
                        EmailLoginScreen(
                            uiState = emailAuthUiState,
                            updateState = emailAuthViewModel::updateState,
                            onSignInClick = onEmailLoginClick,
                            deviceType = deviceType
                        )
                    }
                }

                else -> throw IllegalStateException()
            }
        }
    }
}

@Suppress("UnusedReceiverParameter")
private fun MessengerActivity.loginWithEmail(
    focusManager: FocusManager,
    emailAuthViewModel: EmailAuthViewModel,
    onDone: () -> Unit
): () -> Unit {
    val onEmailLoginClick: () -> Unit = {
        focusManager.clearFocus(true)
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
    return onEmailLoginClick
}

@Suppress("UnusedReceiverParameter")
private fun MessengerActivity.signInWithEmail(
    focusManager: FocusManager,
    emailAuthViewModel: EmailAuthViewModel,
    onDone: () -> Unit
): () -> Unit {
    val onEmailSignInClick: () -> Unit = {
        focusManager.clearFocus(true)
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
    return onEmailSignInClick
}

private fun MessengerActivity.signInWithGitHub(
    gitHubAuthViewModel: GitHubAuthViewModel,
    onDone: () -> Unit
): () -> Unit {
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
    return githubSignin
}

private fun MessengerActivity.signInWithGoogle(
    googleAuthViewModel: GoogleAuthViewModel,
    onDone: () -> Unit
): () -> Unit {
    val googleSignin: () -> Unit = {
        lifecycleScope.launch {
            FirebaseGoogleAuthentication.launchCredentialManagerBottomSheet(this@signInWithGoogle) { credential ->
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
    return googleSignin
}

@Composable
fun rememberWindowSize(): DeviceType {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val adaptiveInfo = currentWindowAdaptiveInfo()

    val containerSize = windowInfo.containerSize

    val width = with(density) { containerSize.width.toDp() }
    val height = with(density) { containerSize.height.toDp() }

    val windowSizeClass = WindowSizeClass.BREAKPOINTS_V1.computeWindowSizeClass(
        widthDp = width.value,
        heightDp = height.value
    )

    val windowAdaptiveInfo = WindowAdaptiveInfo(windowSizeClass, adaptiveInfo.windowPosture)

    return DeviceType.fromWindowAdaptiveInfo(windowAdaptiveInfo)
}