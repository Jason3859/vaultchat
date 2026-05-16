package dev.jason.app.compose.vaultchat.messaging.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.core.domain.Message
import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.MessagingActivity
import dev.jason.app.compose.vaultchat.messaging.domain.MessagingState
import dev.jason.app.compose.vaultchat.messaging.domain.repository.LocalStorageRepository
import dev.jason.app.compose.vaultchat.messaging.domain.repository.RemoteApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDateTime

class PushNotificationService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val apiRepository: RemoteApiRepository by inject()
    private val storageRepository: LocalStorageRepository by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        try {
            coroutineScope.launch {
                Firebase.auth.currentUser?.let { firebaseUser ->
                    val currentDevice = MessagingState.currentDevice.value ?: run {
                        Log.w("PushNotificationService", "onNewToken: current device is null")
                        return@launch
                    }
                    apiRepository.updateToken(firebaseUser.uid, token, currentDevice)
                }
            }
        } catch (e: NullPointerException) {
            Log.w("PushNotificationService", "onNewToken: exception", e)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val type = message.data["type"]

        when (type) {
            "message" -> handleMessage(message.data)
            "status_update" -> handleStatusUpdate(message.data)
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        Firebase.auth.currentUser?.let { firebaseUser ->
            coroutineScope.launch {
                apiRepository.updateStatus(firebaseUser.uid, User.Status.Offline)
            }
        }
    }

    private fun String.toLocalDateTime() = LocalDateTime.parse(this)

    private fun handleMessage(data: Map<String, String>) {
        val from = data["received_from"]!!
        val to = data["to"]!!
        val text = data["text"]!!
        val timestamp = data["timestamp"]!!.toLocalDateTime()

        showNotification(text, from)

        coroutineScope.launch {
            storageRepository.addMessage(Message(from, to, text, timestamp))
        }
    }

    private fun showNotification(text: String, from: String) {
        val intent = Intent(this, MessagingActivity::class.java).apply {
            putExtra("nav_destination", "messaging")
            putExtra("id", from)

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)

        val notification = NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
            .setContentTitle("New message")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // FIXME: to be replaced
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)

        if (MessagingState.otherUserUid.value != from) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    private fun handleStatusUpdate(data: Map<String, String>) {
        val uid = data["uid"]!!
        val status = User.Status.valueOf(data["status"]!!)

        MessagingState.updateConnectionsStatus(uid, status)
    }
}