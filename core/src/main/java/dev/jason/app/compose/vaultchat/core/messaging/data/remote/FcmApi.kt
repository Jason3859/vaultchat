package dev.jason.app.compose.vaultchat.core.messaging.data.remote

import dev.jason.app.compose.vaultchat.core.messaging.data.dto.MessageDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.ApiResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(@Body body: MessageDto): ApiResult<Void> // no data is returned

    @POST("/users/register") // updates fcm token is user already exists
    suspend fun registerUser(body: RegisterUserDto)

    @GET("/users/search")
    suspend fun searchUsers(@Query("name") name: String, @Query("from") from: String): ApiResult<List<UserDto>>

    @GET("/users/get-connections")
    suspend fun getConnections(@Query("uid") uid: String): ApiResult<List<UserDto>>
}