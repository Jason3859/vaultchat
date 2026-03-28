package dev.jason.app.compose.vaultchat.core.messaging.data.remote

import dev.jason.app.compose.vaultchat.core.messaging.data.dto.MessageDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserTokenDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(@Body body: MessageDto)

    @POST("/update-token")
    suspend fun updateFcmToken(@Body body: UserTokenDto)

    @GET("/search-users/{name}")
    suspend fun searchUsers(@Path("name") name: String, @Query("from") from: String): List<UserDto>

    @GET("/get-connections/{uid}")
    suspend fun getConnections(@Path("uid") uid: String): List<UserDto>
}