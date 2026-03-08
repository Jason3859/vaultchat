package dev.jason.app.compose.messaging.data

import dev.jason.app.compose.messaging.domain.Message
import dev.jason.app.compose.messaging.domain.RemoteApi

class RemoteApiImpl(private val fcmApi: FcmApi) : RemoteApi {
    override suspend fun send(body: Message) {
        fcmApi.send(body)
    }
}