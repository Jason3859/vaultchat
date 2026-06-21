package dev.jason.app.compose.vaultchat.feature.messaging

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.core.model.Message
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.UserDto
import dev.jason.app.compose.vaultchat.core.model.toUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

@SuppressLint("MissingFirebaseInstanceTokenRefresh") // TODO
class PushNotificationService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val type = message.data["type"]

        when (type) {
            "message" -> handleMessage(message.data)
            "status_update" -> handleStatusUpdate(message.data)
            "logout_request" -> handleLogout(message.data)
            "connections_update" -> handleConnectionsUpdate(message.data)
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
            val message = Message(from, to, text, timestamp)
            AppEvents.sendEvent(AppEvent.AddMessage(message))
        }
    }

    private fun showNotification(text: String, from: String) {
        val channelId = getString(R.string.notification_channel_id)
        val channelName = getString(R.string.notification_channel_name)
        val channelDescription = getString(R.string.notification_channel_description)

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = channelDescription
            enableVibration(true)
        }

        val intent = Intent("dev.jason.app.compose.vaultchat.main.ACTION_START_MAIN_ACTIVITY").apply {
            putExtra("nav_destination", "messaging")
            putExtra("uid", from)

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("New message")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // FIXME: to be replaced
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        if (AppState.otherUser.value?.uid != from) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    private fun handleStatusUpdate(data: Map<String, String>) {
        val uid = data["uid"]!!
        val status = User.Status.valueOf(data["status"]!!)

        coroutineScope.launch {
            AppEvents.sendEvent(AppEvent.UpdateConnectionStatus(uid, status))
        }
    }

    private fun handleLogout(data: Map<String, String>) {
        val clearMessages = data["clear_messages"].toBoolean()

        coroutineScope.launch {
            if (clearMessages) {
                AppEvents.sendEvent(AppEvent.DeleteAllMessages)
            }

            Firebase.auth.signOut()

            if (AppState.isAppInForeground.value) {
                val intent = packageManager.getLaunchIntentForPackage(packageName)
                intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            delay(500)
            Runtime.getRuntime().exit(0)
        }
    }

    private fun handleConnectionsUpdate(data: Map<String, String>) {
        val connections = data["connections"]!!.let { connectionsJsonString ->
            Json.decodeFromString<List<UserDto>>(connectionsJsonString)
                .map(UserDto::toUser)
        }

        AppEvents.sendEvent(AppEvent.UpdateConnections(connections))
    }
}