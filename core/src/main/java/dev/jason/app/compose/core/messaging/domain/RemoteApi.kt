package dev.jason.app.compose.core.messaging.domain

interface RemoteApi {
    suspend fun send(body: Message)
    suspend fun updateFcmToken(body: UserToken)
}