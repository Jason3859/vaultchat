package dev.jason.app.compose.messaging.service

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.jason.app.compose.messaging.domain.RemoteApi
import dev.jason.app.compose.messaging.domain.UserToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PushNotificationService : FirebaseMessagingService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val remoteApi: RemoteApi by inject()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val user = Firebase.auth.currentUser!!

        coroutineScope.launch {
            remoteApi.updateFcmToken(UserToken(user.uid, token))
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        // TODO: Respond to received message
    }
}