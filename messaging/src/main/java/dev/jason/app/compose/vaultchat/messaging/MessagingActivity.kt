package dev.jason.app.compose.vaultchat.messaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import dev.jason.app.compose.vaultchat.messaging.ui.HomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.time.Duration.Companion.seconds

class MessagingActivity : ComponentActivity() {

    private val apiRepository: RemoteApiRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HomeScreen()
        }
    }

    init {
        lifecycleScope.launch {
            Firebase.auth.currentUser?.let { firebaseUser ->
                while (true) {
                    apiRepository.heartbeat(firebaseUser.uid)
                    delay(30.seconds)
                }
            }
        }
    }
}