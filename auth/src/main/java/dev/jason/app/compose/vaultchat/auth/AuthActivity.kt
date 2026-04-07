package dev.jason.app.compose.vaultchat.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dev.jason.app.compose.vaultchat.auth.data.FirebaseGoogleAuthentication
import dev.jason.app.compose.vaultchat.auth.ui.ExampleSignInScreen
import dev.jason.app.compose.vaultchat.auth.ui.SnackbarController
import kotlinx.coroutines.launch

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val snackbarHostState = SnackbarHostState()

            LaunchedEffect(true) {
                SnackbarController.events.collect { event ->
                    event?.let {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }

            BackHandler {
                finishAffinity() // closes app
            }

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(snackbarHostState) }
            ) { innerPadding ->
                ExampleSignInScreen(
                    onSignInClick = { signInWithGoogle() },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }
        }
    }

    private fun signInWithGoogle() {
        lifecycleScope.launch {
            FirebaseGoogleAuthentication.beginSignIn(this@AuthActivity)
                ?.addOnSuccessListener {
                    finish() // takes back to MainActivity which launches MessagingActivity
                }
                ?.addOnFailureListener { exception ->
                    Log.w("SignInScreen", "signInWithGoogle: exception while signing in with google", exception)
                    SnackbarController.sendEvent(exception.localizedMessage!!)
                }
        }
    }
}