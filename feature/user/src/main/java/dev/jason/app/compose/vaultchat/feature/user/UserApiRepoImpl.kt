package dev.jason.app.compose.vaultchat.feature.user

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class UserApiRepoImpl(
    private val client: HttpClient
) : UserApiRepository {

    override suspend fun heartbeat(uid: String) {
        client.patch("$BASE_URL/user/heartbeat") {
            parameter("uid", uid)
        }
    }

    override suspend fun registerUser(user: User): UserApiRepository.StatusCode {
        val response = client.post("$BASE_URL/user/register") {
            contentType(ContentType.Application.Json)
            setBody(user.toDto())
        }

        return response.status.value
    }

    override suspend fun searchUser(displayName: String): List<User> {
        val response = client.get("$BASE_URL/social/search") {
            parameter("search_query", displayName)
        }

        return response.body()
    }
}