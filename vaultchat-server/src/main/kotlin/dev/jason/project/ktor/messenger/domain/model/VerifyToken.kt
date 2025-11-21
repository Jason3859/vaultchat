package dev.jason.project.ktor.messenger.domain.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.server.routing.Routing

@Suppress("UnusedReceiverParameter")
fun Routing.verifyToken(token: String): FirebaseToken? {
    return try {
        FirebaseAuth.getInstance().verifyIdToken(token)
    } catch (_: FirebaseAuthException) {
        null
    }
}