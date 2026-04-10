package dev.jason.app.compose.vaultchat.auth.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dev.jason.app.compose.vaultchat.auth.R
import dev.jason.app.compose.vaultchat.auth.ui.SnackbarController

object FirebaseGoogleAuthentication {
    suspend fun beginSignIn(context: Context): Task<AuthResult?>? {
        try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = CredentialManager.create(context).getCredential(context, request)

            if (result.credential is CustomCredential && result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                return Firebase.auth.signInWithCredential(firebaseCredential)
            }

            throw IllegalStateException("Credential is not type of Google")

        } catch (e: GetCredentialException) {
            Log.e("FirebaseGoogleAuth", "launchCredentialManagerBottomSheet: exception", e)
            if (e is NoCredentialException) {
                SnackbarController.sendEvent("No Internet")
                return null
            }
            SnackbarController.sendEvent(e.message!!)
            return null
        }
    }
}