package dev.jason.app.compose.core.messaging.data.remote

import dev.jason.app.compose.core.messaging.data.dto.UserDto
import dev.jason.app.compose.core.messaging.data.dto.toDomain
import dev.jason.app.compose.core.messaging.data.dto.toDto
import dev.jason.app.compose.core.messaging.domain.model.Message
import dev.jason.app.compose.core.messaging.domain.remote.RemoteApi
import dev.jason.app.compose.core.messaging.domain.model.User
import dev.jason.app.compose.core.messaging.domain.model.UserToken

class RemoteApiImpl(private val api: FcmApi) : RemoteApi {
    override suspend fun sendMessage(body: Message) {
        api.sendMessage(body.toDto())
    }

    override suspend fun updateFcmToken(body: UserToken) {
        api.updateFcmToken(body.toDto())
    }

    override suspend fun searchUsers(name: String): List<User> {
        return api.searchUsers(name).map(UserDto::toDomain)
    }
}