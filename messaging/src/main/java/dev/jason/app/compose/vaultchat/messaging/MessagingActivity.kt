package dev.jason.app.compose.vaultchat.messaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.domain.MessagingState
import dev.jason.app.compose.vaultchat.messaging.ui.HomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import kotlin.time.Duration.Companion.seconds

class MessagingActivity : ComponentActivity() {

    private val remoteApi: RemoteApi by inject()
    private var isOffline: Boolean = false // TODO: update ui that device is offline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MessagingState.updateCurrentDevice(User.Device.getCurrentDevice(this, "not needed"))

        lifecycleScope.launch {
            Firebase.auth.currentUser?.let { firebaseUser ->
                while (true) {
                    try {
                        remoteApi.heartbeat(firebaseUser.uid)
                        isOffline = false
                    } catch (_: HttpException) {
                        isOffline = true
                    }
                    delay(30.seconds)
                }
            }
        }

        setContent {
            HomeScreen()
        }
    }
}