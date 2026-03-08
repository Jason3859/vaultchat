package dev.jason.app.compose.vaultchat.auth.data

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.R
import dev.jason.app.compose.vaultchat.SnackbarController

object FirebaseGoogleAuthentication {

    fun signInWithIdToken(idToken: String): Task<AuthResult?> {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        return Firebase.auth.signInWithCredential(firebaseCredential)
    }

    fun signInWithCredential(credential: Credential): Task<AuthResult?> {
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            return signInWithIdToken(googleIdTokenCredential.idToken)
        }

        throw IllegalStateException("Credential is not type of Google")
    }

    suspend fun launchCredentialManagerBottomSheet(
        context: Context,
        onResult: (Credential) -> Unit
    ) {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = CredentialManager.create(context).getCredential(context, request)

            onResult(result.credential)

        } catch (e: GetCredentialException) {
            SnackbarController.sendEvent(e.message!!)
            Log.e("FirebaseGoogleAuth", "launchCredentialManagerBottomSheet: exception", e)
        }
    }
}