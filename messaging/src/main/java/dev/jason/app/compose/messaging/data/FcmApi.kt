package dev.jason.app.compose.messaging.data

import dev.jason.app.compose.messaging.domain.Message
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/send")
    suspend fun send(@Body body: Message)
}