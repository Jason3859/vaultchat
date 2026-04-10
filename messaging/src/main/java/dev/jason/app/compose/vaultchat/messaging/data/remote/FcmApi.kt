package dev.jason.app.compose.vaultchat.messaging.data.remote

import dev.jason.app.compose.vaultchat.core.domain.User
import dev.jason.app.compose.vaultchat.messaging.data.dto.MessageDto
import dev.jason.app.compose.vaultchat.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.messaging.domain.model.ApiResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(@Body body: MessageDto): ApiResult<Void> // no data is returned

    @GET("/user/search")
    suspend fun searchUsers(@Query("name") name: String, @Query("from") from: String): ApiResult<List<UserDto>>

    @GET("/user/get-connections")
    suspend fun getConnections(@Query("uid") uid: String): ApiResult<List<UserDto>>

    @PUT("/user/update-status")
    suspend fun updateStatus(@Query("uid") uid: String, @Query("status") status: User.Status): ApiResult<Void>

    @PUT("/user/heartbeat")
    suspend fun heartbeat(@Query("uid") uid: String): ApiResult<Void>
}