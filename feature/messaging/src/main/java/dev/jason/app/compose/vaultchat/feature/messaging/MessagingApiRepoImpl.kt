package dev.jason.app.compose.vaultchat.feature.messaging

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dev.jason.app.compose.vaultchat.core.AppConstants.ACTION_START_MAIN_ACTIVITY
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_WS_URL
import dev.jason.app.compose.vaultchat.core.AppConstants.EXTRA_NAV_DESTINATION_KEY
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppNotificationChannelConfig
import dev.jason.app.compose.vaultchat.core.AppNotificationManager
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.R
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.message.MessageDto
import dev.jason.app.compose.vaultchat.core.model.message.toDto
import dev.jason.app.compose.vaultchat.core.model.message.toMessage
import dev.jason.app.compose.vaultchat.core.model.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText

class MessagingApiRepoImpl(
    private val client: StompClient,
    private val context: Context
) : MessagingApiRepository {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var webSocketSession: StompSession

    init {
        coroutineScope.launch {
            connectToWebSocketServer()
            subscribeToMessages()
            subscribeToConnectionStatusUpdates()
        }
    }

    override suspend fun sendMessage(message: Message) {
        webSocketSession.sendText("/app/send", Json.encodeToString(message.toDto()))
    }

    private suspend fun connectToWebSocketServer() {
        try {
            webSocketSession =
                client.connect("$BASE_WS_URL/messages?uid=${AppState.currentUser.value?.uid!!}")
        } catch (e: Exception) {
            Log.e("MessagingApiRepoImpl", "connectToWebSocketServer: websocket connection error", e)
            ToastController.showToast("error while connecting to websocket")
        }
    }

    private suspend fun subscribeToMessages() {
        webSocketSession.subscribeText("/user/topic/messages").collect { messageJsonString ->
            val message = Json.decodeFromString<MessageDto>(messageJsonString).toMessage()
            showNotification(message)
            AppEvents.sendEvent(AppEvent.AddMessage(message))
        }
    }

    private suspend fun subscribeToConnectionStatusUpdates() {
        webSocketSession.subscribeText("/user/topic/connections-status")
            .collect { connectionsStatusUpdateJsonString ->
                val map = Json.decodeFromString<Map<String, String>>(connectionsStatusUpdateJsonString)

                val uid = map["uid"] ?: run {
                    Log.w(
                        "MessagingApiRepoImpl",
                        "subscribeToConnectionStatusUpdates: invalid update received: $map"
                    )
                    return@collect
                }
                val status = User.Status.valueOf(map["status"]!!)

                AppEvents.sendEvent(AppEvent.UpdateConnectionStatus(uid, status))
            }
    }

    private fun showNotification(message: Message) {

        val intent = Intent(ACTION_START_MAIN_ACTIVITY).apply {
            putExtra(EXTRA_NAV_DESTINATION_KEY, "messaging")
            putExtra("uid", message.from)

            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, flags)

        val notification = NotificationCompat.Builder(context, "notificationChannelConfig.id")
            .setContentTitle("New message")
            .setContentText(message.text)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // FIXME: to be replaced
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            AppNotificationManager(AppNotificationChannelConfig.messagesNotificationChannel(context))

        if (AppState.otherUser.value?.uid != message.from) {
            notificationManager.showNotification(context, notification)
        } else {
            if (!AppState.isAppInForeground.value) {
                notificationManager.showNotification(context, notification)
            }
        }
    }
}