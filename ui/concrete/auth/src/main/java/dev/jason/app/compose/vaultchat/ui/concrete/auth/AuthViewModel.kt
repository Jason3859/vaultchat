package dev.jason.app.compose.vaultchat.ui.concrete.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.messaging
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.User
import dev.jason.app.compose.vaultchat.core.model.toDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val remoteApiRepository: RemoteApiRepository
) : ViewModel() {

    /**
     * sends request to backend to add user first.
     * if user is already existing, it adds device to backend.
     */
    fun registerUser(user: FirebaseUser, context: Context): Job {
        return viewModelScope.launch {
            val token = Firebase.messaging.token.await()

            val currentUser = User(
                uid = user.uid,
                displayName = user.displayName!!,
                profilePictureUrl = user.photoUrl?.toString()!!,
                status = User.Status.Online
            )

            val currentDevice = Device.getCurrentDevice(context, user.uid, token)

            remoteApiRepository.registerUser(currentUser.toDto(currentDevice.toDto())).let { statusCode ->
                Log.d("AuthViewModel", "registerUser: status code: $statusCode")
                if (statusCode !in 200..299) {
                    remoteApiRepository.addDevice(currentDevice.toDto())
                }
            }
        }
    }
}