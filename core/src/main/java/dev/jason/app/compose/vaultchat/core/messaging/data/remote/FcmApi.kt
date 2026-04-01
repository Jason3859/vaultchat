package dev.jason.app.compose.vaultchat.core.messaging.data.remote

import dev.jason.app.compose.vaultchat.core.messaging.data.dto.MessageDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserDto
import dev.jason.app.compose.vaultchat.core.messaging.data.dto.UserTokenDto
import dev.jason.app.compose.vaultchat.core.messaging.domain.model.ApiResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FcmApi {

    @POST("/send")
    suspend fun sendMessage(@Body body: MessageDto): ApiResult<Void> // no data is returned

    @POST("/update-token")
    suspend fun updateFcmToken(@Body body: UserTokenDto): ApiResult<Void> // no data is returned

    @GET("/search-users")
    suspend fun searchUsers(@Query("name") name: String, @Query("from") from: String): ApiResult<List<UserDto>>

    @GET("/get-connections")
    suspend fun getConnections(@Query("uid") uid: String): ApiResult<List<UserDto>>
}