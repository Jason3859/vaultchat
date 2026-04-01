package dev.jason.app.compose.vaultchat.core.messaging.domain.service

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
import dev.jason.app.compose.vaultchat.core.messaging.domain.CurrentScreen
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

        val from = message.data["receivedFrom"]!!
        val to = message.data["to"]!!
        val text = message.data["text"]!!
        val timestamp = message.data["timestamp"]!!.toLocalDateTime()

        val notification = NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle("New message")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

        val notificationManager = getSystemService(NotificationManager::class.java)

        if (CurrentScreen.otherUserUid != from) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }

        coroutineScope.launch {
            repository.addMessage(Message(from, to, text, timestamp))
        }
    }

    private fun String.toLocalDateTime() = LocalDateTime.parse(this)
}