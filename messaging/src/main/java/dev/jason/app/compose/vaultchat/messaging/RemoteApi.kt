package dev.jason.app.compose.vaultchat.messaging

import retrofit2.http.PUT
import retrofit2.http.Query

interface RemoteApi {

    @PUT("/user/heartbeat")
    suspend fun heartbeat(@Query("uid") uid: String)
}