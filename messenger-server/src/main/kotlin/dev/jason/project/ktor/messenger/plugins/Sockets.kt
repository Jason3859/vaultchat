package dev.jason.project.ktor.messenger.plugins

import dev.jason.project.ktor.messenger.data.model.MessageDto
import dev.jason.project.ktor.messenger.data.model.UserDto
import dev.jason.project.ktor.messenger.data.model.toDto
import dev.jason.project.ktor.messenger.data.model.toLong
import dev.jason.project.ktor.messenger.domain.db.MessagesDatabaseRepository
import dev.jason.project.ktor.messenger.domain.model.Message
import dev.jason.project.ktor.messenger.domain.model.verifyToken
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets() {
    val messagesDbRepository by inject<MessagesDatabaseRepository>()
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        val chatSessions = ConcurrentHashMap<String, MutableList<DefaultWebSocketServerSession>>()

        webSocket("/chat/{roomId}") {
            val roomId = call.parameters["RoomId"]
            val token = call.request.headers["Token"]

            if (token == null) {
                close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "Token header is missing or null"
                    )
                )
                return@webSocket
            }

            val decoded = verifyToken(token)

            if (decoded == null) {
                close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Invalid token"))
                return@webSocket
            }

            val username = decoded.name

            if (roomId == null) {
                close(
                    CloseReason(
                        CloseReason.Codes.CANNOT_ACCEPT,
                        "RoomId path parameter is missing"
                    )
                )
                return@webSocket
            }

            val sessionList = chatSessions.getOrPut(roomId) { mutableListOf() }
            sessionList.add(this)

            println("User $username connected to chat $roomId")
            sessionList.forEach { session ->
                session.send(
                    Json.encodeToString(
                        MessageDto(
                            id = Random.nextLong(),
                            roomId = roomId,
                            sender = UserDto("server@3859✓", null),
                            message = "User $username connected to the chat",
                            timestamp = LocalDateTime.now().toLong()
                        )
                    )
                )
            }

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        val message = Message(
                            id = Random.nextLong(),
                            roomId = roomId,
                            senderUid = decoded.uid,
                            message = text,
                            timestamp = LocalDateTime.now()
                        )
                        messagesDbRepository.addMessage(message)
                        launch(Dispatchers.IO) {
                            sessionList.forEach { session ->
                                if (session != this) {
                                    session.send(Json.encodeToString(message.toDto()))
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error for user $username: ${e.localizedMessage}")
                e.printStackTrace()
            } 
        }
    }
}
