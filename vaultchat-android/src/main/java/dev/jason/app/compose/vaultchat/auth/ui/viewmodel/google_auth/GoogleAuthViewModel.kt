package dev.jason.app.compose.vaultchat.auth.ui.viewmodel.google_auth

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import dev.jason.app.compose.vaultchat.auth.data.google.FirebaseGoogleAuthentication

class GoogleAuthViewModel(private val firebaseGoogleAuthentication: FirebaseGoogleAuthentication) : ViewModel() {

    fun signin(credential: Credential): Task<AuthResult?>? {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return firebaseGoogleAuthentication.signin(googleIdTokenCredential.idToken)
        } else {
            Log.w("GoogleAuthViewModel", "signin: Credential is not a type of Google")
            return null
        }
    }
}