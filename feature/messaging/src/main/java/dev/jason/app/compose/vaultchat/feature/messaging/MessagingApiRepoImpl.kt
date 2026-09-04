package dev.jason.app.compose.vaultchat.feature.messaging

import android.util.Log
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_HTTP_URL
import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_WS_URL
import dev.jason.app.compose.vaultchat.core.AppEvent
import dev.jason.app.compose.vaultchat.core.AppEvent.ShowNotification
import dev.jason.app.compose.vaultchat.core.AppEvents
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.NotificationChannel
import dev.jason.app.compose.vaultchat.core.ToastController
import dev.jason.app.compose.vaultchat.core.model.message.Message
import dev.jason.app.compose.vaultchat.core.model.message.MessageDto
import dev.jason.app.compose.vaultchat.core.model.message.toDto
import dev.jason.app.compose.vaultchat.core.model.message.toMessage
import dev.jason.app.compose.vaultchat.core.model.user.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText

class MessagingApiRepoImpl(
    private val stompClient: StompClient,
    private val httpClient: HttpClient,
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

    override suspend fun fetchMessages(): List<Message> {
        val urlString = "$BASE_HTTP_URL/messages/fetch?uid=${AppState.currentUser.value?.uid!!}"
        return httpClient.get(urlString)
            .apply { bodyAsText().let(::println) }
            .body<List<MessageDto>>()
            .map(MessageDto::toMessage)
    }

    private suspend fun connectToWebSocketServer() {
        try {
            val url = "$BASE_WS_URL/messages?uid=${AppState.currentUser.value?.uid!!}"
            webSocketSession = stompClient.connect(url)
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
                val map =
                    Json.decodeFromString<Map<String, String>>(connectionsStatusUpdateJsonString)

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
        if (message.from != AppState.currentUser.value?.uid) {
            if (AppState.otherUser.value?.uid != message.from) {
                sendShowNotificationEvent(message)
            } else {
                if (!AppState.isAppInForeground.value) {
                    sendShowNotificationEvent(message)
                }
            }
        }
    }

    private fun sendShowNotificationEvent(message: Message) {
        AppEvents.sendEvent(
            ShowNotification(
                title = "New message",
                content = message.text,
                channel = NotificationChannel.MESSAGES,
                extras = listOf(message.from, message.fromDisplayName)
            )
        )
    }
}