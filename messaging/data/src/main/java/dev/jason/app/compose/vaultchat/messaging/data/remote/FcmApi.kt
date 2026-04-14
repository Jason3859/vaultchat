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

    @POST("/user/update-token")
    suspend fun updateToken(@Query("uid") uid: String, @Query("token") token: String, @Body deviceDto: UserDto.DeviceDto): ApiResult<Void>

    @GET("/devices/my-devices")
    suspend fun fetchDevices(@Query("uid") uid: String): ApiResult<List<UserDto.DeviceDto>>

    @GET("/user/get-blocked-users")
    suspend fun fetchBlocklist(@Query("uid") uid: String): ApiResult<List<UserDto>>

    @POST("/user/block")
    suspend fun block(@Query("uid") uid: String, @Query("uid_to_block") uidToBlock: String): ApiResult<Void>

    @POST("/user/unblock")
    suspend fun unblock(@Query("uid") uid: String, @Query("uid_to_unblock") uidToUnblock: String): ApiResult<Void>
}