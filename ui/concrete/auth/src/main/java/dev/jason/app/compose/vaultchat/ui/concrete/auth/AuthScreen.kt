package dev.jason.app.compose.vaultchat.ui.concrete.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.ui.abstractt.auth.AbstractAuthScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    val backStack = rememberNavBackStack(Route.AUTH)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(Route.AUTH) {
                AbstractAuthScreen(
                    onAuthSuccess = onAuthSuccess,
                    navigateToLoading = {
                        backStack.apply {
                            add(Route.LOADING)
                            remove(Route.AUTH)
                        }
                    }
                )
            }

            entry(Route.LOADING) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingIndicator(
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = stringResource(R.string.logging_in),
                        fontSize = 20.sp
                    )
                }
            }
        }
    )
}

@Composable
private fun AbstractAuthScreen(
    navigateToLoading: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    val viewModel: AuthViewModel = koinViewModel()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    AbstractAuthScreen(
        onSignInClick = {
            coroutineScope.launch {
                FirebaseGoogleAuthentication.beginSignIn(context)
                    ?.addOnFailureListener { exception ->
                        ToastController.showToast("An error occurred: ${exception.message}")
                        Log.e(
                            "ConcreteAuthScreen",
                            "ConcreteAuthScreen: exception occurred",
                            exception
                        )
                    }
                    ?.addOnSuccessListener {
                        navigateToLoading.invoke()

                        viewModel.registerUser(
                            user = it?.user!!,
                            context = context
                        ).invokeOnCompletion {
                            onAuthSuccess.invoke()
                        }
                    }
            }
        }
    )
}