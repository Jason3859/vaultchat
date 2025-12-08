package dev.jason.app.compose.vaultchat.web_socket.domain.repository

import dev.jason.app.compose.vaultchat.web_socket.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface WsConnectionRepository {

    suspend fun connect(roomId: String)
    suspend fun disconnect()
    suspend fun getMessages(): Flow<Message>
}