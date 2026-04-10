package dev.jason.app.compose.vaultchat.auth.data

import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteApi {

    @POST("/user/register")
    suspend fun registerUser(@Body body: RegisterUserDto)
}