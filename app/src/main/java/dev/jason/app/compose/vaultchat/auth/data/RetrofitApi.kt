package dev.jason.app.compose.vaultchat.auth.data

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApi {

    @POST("/add-user")
    suspend fun addUser(@Body body: UserDto)
}

@Serializable
data class UserDto(
    val uid: String,
    val displayName: String,
    val profilePictureUrl: String,
    val fcmToken: String
)

fun User.toDto(): UserDto = UserDto(uid, displayName, profilePictureUrl, fcmToken)