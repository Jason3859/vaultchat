package dev.jason.app.compose.vaultchat.auth.data.github

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth

class FirebaseGitHubAuthentication {

    private val provider = OAuthProvider.newBuilder("github.com").build()

    private val auth = Firebase.auth

    private val pending = auth.pendingAuthResult

    fun signin(activity: Activity): Task<AuthResult?> {
        return pending ?: auth.startActivityForSignInWithProvider(activity, provider)
    }
}