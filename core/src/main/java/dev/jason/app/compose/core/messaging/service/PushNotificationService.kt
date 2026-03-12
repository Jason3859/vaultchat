package dev.jason.app.compose.core.messaging.service

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.jason.app.compose.core.messaging.domain.RemoteApi
import dev.jason.app.compose.core.messaging.domain.UserToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PushNotificationService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val remoteApi: RemoteApi by inject()

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

        // TODO: Respond to received message
    }
}