package dev.jason.app.compose.messaging.domain

interface RemoteApi {
    suspend fun send(body: Message)
    suspend fun updateFcmToken(userToken: UserToken)
}