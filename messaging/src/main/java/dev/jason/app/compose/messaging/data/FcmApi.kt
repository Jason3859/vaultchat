package dev.jason.app.compose.messaging.data

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {

    @POST("/send")
    suspend fun send(@Body body: MessageDto)

    @POST("/update-token")
    suspend fun updateFcmToken(@Body body: UserTokenDto)
}