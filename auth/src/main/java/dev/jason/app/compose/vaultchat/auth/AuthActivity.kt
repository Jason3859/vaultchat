package dev.jason.app.compose.vaultchat.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.messaging
import dev.jason.app.compose.vaultchat.auth.data.FirebaseGoogleAuthentication
import dev.jason.app.compose.vaultchat.auth.data.RegisterUserDto
import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.ui.AuthViewModel
import dev.jason.app.compose.vaultchat.auth.ui.ExampleSignInScreen
import dev.jason.app.compose.vaultchat.auth.ui.SnackbarController
import dev.jason.app.compose.vaultchat.messaging.MessagingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.koin.android.ext.android.inject


class AuthActivity : ComponentActivity() {

    private val remoteApi: RemoteApi by inject()
    private lateinit var token: String

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        lifecycleScope.launch {
            token = Firebase.messaging.token.await()
        }

        Firebase.auth.currentUser?.let {
            startActivity(Intent(this, MessagingActivity::class.java))
            finish()
        }

        setContent {
            val snackbarHostState = SnackbarHostState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            LaunchedEffect(true) {
                SnackbarController.events.collect { event ->
                    event?.let {
                        snackbarHostState.showSnackbar(it)
                    }
                }
            }

            when (currentScreen) {
                AuthViewModel.Screen.Auth -> {
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

                AuthViewModel.Screen.Loading -> {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        lifecycleScope.launch {
            FirebaseGoogleAuthentication.beginSignIn(this@AuthActivity)
                ?.addOnSuccessListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.updateCurrentScreen(AuthViewModel.Screen.Loading)
                        remoteApi.registerUser(
                            RegisterUserDto(
                                it?.user?.uid!!,
                                it.user?.displayName!!,
                                it.user?.photoUrl.toString(),
                                RegisterUserDto.DeviceDto(
                                    "${Build.MANUFACTURER} ${Build.MODEL}",
                                    version = "${Build.VERSION.RELEASE}",
                                    fcmToken = token,
                                    type = getDeviceType()
                                )
                            )
                        )
                        startActivity(Intent(this@AuthActivity, MessagingActivity::class.java))
                        finish()
                    }
                }
                ?.addOnFailureListener { exception ->
                    Log.e(
                        "SignInScreen",
                        "signInWithGoogle: exception while signing in with google",
                        exception
                    )
                    SnackbarController.sendEvent(exception.localizedMessage!!)
                }
        }
    }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun getDeviceType(): RegisterUserDto.DeviceDto.Type {
        val screenLayout = resources.configuration.screenLayout
        val mask = Configuration.SCREENLAYOUT_SIZE_MASK
        val isTablet = (screenLayout and mask) >= Configuration.SCREENLAYOUT_SIZE_LARGE
        return if (isTablet) {
            RegisterUserDto.DeviceDto.Type.Tablet
        } else {
            RegisterUserDto.DeviceDto.Type.Mobile
        }
    }
}