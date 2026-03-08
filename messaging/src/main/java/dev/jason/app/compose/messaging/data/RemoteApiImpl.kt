package dev.jason.app.compose.messaging.data

import dev.jason.app.compose.messaging.domain.Message
import dev.jason.app.compose.messaging.domain.RemoteApi
import dev.jason.app.compose.messaging.domain.UserToken

class RemoteApiImpl(private val fcmApi: FcmApi) : RemoteApi {
    override suspend fun send(body: Message) {
        fcmApi.send(body)
    }

    override suspend fun updateFcmToken(body: UserToken) {
        fcmApi.updateFcmToken(body)
    }
}