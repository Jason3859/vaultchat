package dev.jason.app.compose.vaultchat.core.messaging.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.local_storage.messages.domain.MessageRepository
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.UserToken
import dev.jason.app.compose.vaultchat.core.messaging.domain.remote.RemoteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime

class PushNotificationService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val remoteApi: RemoteApi by inject()
    private val repository: MessageRepository by inject()


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        try {
            val user = Firebase.auth.currentUser!!

            coroutineScope.launch {
                remoteApi.updateFcmToken(UserToken(user.uid, token))
                Log.d("PushNotificationService", "onNewToken: sent new token to server")
            }
        } catch (e: NullPointerException) {
            Log.w("PushNotificationService", "onNewToken: exception", e)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(
            "PushNotificationService",
            "onMessageReceived: message received : ${message.data["text"]}"
        )

        val notification =
            NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle("New message")
                .setContentText(message.data["text"])
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)

        coroutineScope.launch {
            repository.addMessage(
                Message(
                    from = message.data["receivedFrom"]!!,
                    to = message.data["to"]!!,
                    text = message.data["text"]!!,
                    timestamp = message.data["timestamp"]!!.toLocalDateTime()
                )
            )
        }
    }

    private fun String.toLocalDateTime() = LocalDateTime.parse(this)
}