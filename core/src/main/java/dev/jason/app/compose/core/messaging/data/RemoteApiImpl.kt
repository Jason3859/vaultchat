package dev.jason.app.compose.core.messaging.data

import dev.jason.app.compose.core.messaging.domain.Message
import dev.jason.app.compose.core.messaging.domain.RemoteApi
import dev.jason.app.compose.core.messaging.domain.UserToken

class RemoteApiImpl(private val fcmApi: FcmApi) : RemoteApi {
    override suspend fun send(body: Message) {
        fcmApi.send(body.toDto())
    }

    override suspend fun updateFcmToken(body: UserToken) {
        fcmApi.updateFcmToken(body.toDto())
    }
}