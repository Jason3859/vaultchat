package dev.jason.app.compose.vaultchat.auth.ui.viewmodel.github_auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dev.jason.app.compose.vaultchat.auth.data.github.FirebaseGitHubAuthentication

class GitHubAuthViewModel(
    private val firebaseGitHubAuthentication: FirebaseGitHubAuthentication
) : ViewModel() {

    fun signin(activity: Activity): Task<AuthResult?> {
        return firebaseGitHubAuthentication.signin(activity)
    }
}