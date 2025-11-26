package dev.jason.app.compose.vaultchat.auth.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import dev.jason.app.compose.vaultchat.MessengerActivity
import dev.jason.app.compose.vaultchat.auth.data.google.FirebaseGoogleAuthentication
import dev.jason.app.compose.vaultchat.auth.ui.screen.LoginScreen
import dev.jason.app.compose.vaultchat.auth.ui.util.SnackbarController
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.github_auth.GitHubAuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.viewmodel.google_auth.GoogleAuthViewModel
import dev.jason.app.compose.vaultchat.util.DeviceType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MessengerActivity.AuthNavGraph(deviceType: DeviceType, onDone: () -> Unit) {

    val snackbarHostState = remember { SnackbarHostState() }

    val googleAuthViewModel: GoogleAuthViewModel = koinViewModel()
    val gitHubAuthViewModel: GitHubAuthViewModel = koinViewModel()

    val googleSignin: () -> Unit = signInWithGoogle(googleAuthViewModel, onDone)
    val githubSignin: () -> Unit = signInWithGitHub(gitHubAuthViewModel, onDone)

    LaunchedEffect(true) {
        SnackbarController.events.collect { event ->
            snackbarHostState.showSnackbar(event)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        LoginScreen(
            onSignInUsingGoogleClick = googleSignin,
            onSigninUsingGitHubClick = githubSignin,
            deviceType = deviceType,
        )
    }
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