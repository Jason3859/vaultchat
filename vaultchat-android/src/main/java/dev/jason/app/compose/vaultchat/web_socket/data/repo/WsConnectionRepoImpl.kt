package dev.jason.app.compose.vaultchat.web_socket.data.repo

import dev.jason.app.compose.vaultchat.web_socket.domain.model.Message
import dev.jason.app.compose.vaultchat.web_socket.domain.repository.WsConnectionRepository
import kotlinx.coroutines.flow.Flow

class WsConnectionRepoImpl : WsConnectionRepository {

    override suspend fun connect(roomId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun getMessages(): Flow<Message> {
        TODO("Not yet implemented")
    }
}