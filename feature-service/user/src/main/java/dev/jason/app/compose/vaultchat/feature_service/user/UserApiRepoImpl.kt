package dev.jason.app.compose.vaultchat.feature_service.user

import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.toDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters

internal class UserApiRepoImpl(
    private val baseUrl: String,
    private val client: HttpClient
) : UserApiRepository {

    override suspend fun registerUser(user: User) {
        client.post("$baseUrl/user/register") {
            contentType(ContentType.Application.Json)
            setBody(user.toDto())
        }
    }

    override suspend fun searchUser(displayName: String): List<User> {
        val response = client.get("$baseUrl/social/search") {
            parameters {
                append("search_query", displayName)
            }
        }

        return response.body()
    }
}