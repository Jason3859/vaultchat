package dev.jason.project.ktor.messenger.plugins

import dev.jason.project.ktor.messenger.data.model.toDto
import dev.jason.project.ktor.messenger.domain.db.MessagesDatabaseRepository
import dev.jason.project.ktor.messenger.domain.model.verifyToken
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class Rooms(
    val uid: String,
    val roomId: String
)

fun Application.configureRouting() {

    val messagesDbRepository by inject<MessagesDatabaseRepository>()

    routing {
        head("/") {
            call.respond(HttpStatusCode.OK)
        }

        get("/") {
            call.respondText("Hello World!")
        }

        get("/get-messages") {

            val token = call.request.headers["Token"]

            if (token == null) {
                call.respond(HttpStatusCode.BadRequest, "Token header is missing")
                return@get
            }

            verifyToken(token).apply {
                if (this == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Token is invalid or has expired")
                    return@get
                }
            }

            val messages = messagesDbRepository
                .getAllMessages()
                .map { it.toDto() }

            call.respond(HttpStatusCode.Accepted, messages)
        }

        get("/get-rooms") {

            val token = call.request.headers["Token"]

            if (token == null) {
                call.respond(HttpStatusCode.BadRequest, "Token header is missing")
                return@get
            }

            val user = verifyToken(token).apply {
                if (this == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Token is invalid or has expired")
                }
            }

            val rooms = messagesDbRepository
                .getAllMessages()
                .map { message ->
                    Rooms(message.senderUid, message.roomId)
                }
                .filter { it.uid == user?.uid!! }
                .map { it.roomId }
                .distinct()

            call.respond(HttpStatusCode.Accepted, rooms)
        }
    }
}
