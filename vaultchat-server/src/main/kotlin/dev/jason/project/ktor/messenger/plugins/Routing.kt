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
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

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
                    call.respond(HttpStatusCode.Unauthorized, "Invalid token")
                    return@get
                }
            }

            val messages = messagesDbRepository
                .getAllMessages()
                .map { it.toDto() }

            val serialized = Json.encodeToString(messages)

            call.respond(HttpStatusCode.Accepted, serialized)
        }
    }
}
