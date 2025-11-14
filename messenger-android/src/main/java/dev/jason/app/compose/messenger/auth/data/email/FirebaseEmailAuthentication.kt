package dev.jason.app.compose.messenger.auth.data.email

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.auth

class FirebaseEmailAuthentication {

    fun signin(email: String, password: String): Task<AuthResult?> {
        return Firebase.auth.createUserWithEmailAndPassword(email, password)
    }

    fun login(email: String, password: String): Task<AuthResult?> {
        return Firebase.auth.signInWithEmailAndPassword(email, password)
    }
}