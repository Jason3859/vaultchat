package dev.jason.app.compose.vaultchat.auth.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import dev.jason.app.compose.vaultchat.MainActivity
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.SnackbarController
import dev.jason.app.compose.vaultchat.auth.data.FirebaseGoogleAuthentication
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import androidx.core.content.edit

@Composable
fun MainActivity.ExampleSignInScreen(viewModel: MainViewModel = koinViewModel()) {
    // Example SignIn Screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { signInWithGoogle(viewModel) }
        ) {
            Text(stringResource(R.string.sign_in_with_google))
        }
    }
}

fun MainActivity.signInWithGoogle(viewModel: MainViewModel) {
    lifecycleScope.launch {
        FirebaseGoogleAuthentication.launchCredentialManagerBottomSheet(this@signInWithGoogle) { credential ->
            FirebaseGoogleAuthentication.signInWithCredential(credential)
                .addOnSuccessListener {
                    viewModel.onAction(MainViewModel.Action.SignInComplete)
                    sharedPrefs.edit { putBoolean(MainActivity.IS_SIGNED_IN_PREF_NAME, true) }
                }
                .addOnFailureListener { exception ->
                    Log.w("SignInScreen", "signInWithGoogle: exception while signing in with google", exception)
                    SnackbarController.sendEvent(exception.localizedMessage!!)
                }
        }
    }
}