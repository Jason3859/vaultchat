package dev.jason.app.compose.vaultchat.feature.blocklist

import dev.jason.app.compose.vaultchat.core.AppConstants.BASE_URL
import dev.jason.app.compose.vaultchat.core.AppState
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.UserDto
import dev.jason.app.compose.vaultchat.core.model.toUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch

class BlocklistApiRepoImpl(private val client: HttpClient) : BlocklistApiRepository {

    private val currentUserUid by lazy { AppState.currentUser.value?.uid!! }

    override suspend fun getBlocklist(): List<User> {
        val response = client.get("$BASE_URL/social/blocked-users") {
            parameter("uid", currentUserUid)
        }

        return response.body<List<UserDto>>().map(UserDto::toUser)
    }

    override suspend fun blockUser(user: User) {
        client.patch("$BASE_URL/social/block") {
            parameter("from_uid", currentUserUid)
            parameter("other_uid", user.uid)
        }
    }

    override suspend fun unblockUser(user: User) {
        client.patch("$BASE_URL/social/unblock") {
            parameter("from_uid", currentUserUid)
            parameter("other_uid", user.uid)
        }
    }
}