package dev.jason.app.compose.vaultchat.ui.auth.concrete

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.messaging
import dev.jason.app.compose.vaultchat.core.model.Device
import dev.jason.app.compose.vaultchat.core.model.RegisterUserDto
import dev.jason.app.compose.vaultchat.core.model.toDto
import dev.jason.app.compose.vaultchat.feature.user.UserApiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(private val userApiService: UserApiService) : ViewModel() {

    fun registerUser(user: FirebaseUser, context: Context): Job {
        return viewModelScope.launch {
            val token = Firebase.messaging.token.await()

            val currentDevice = Device.getCurrentDevice(context, user.uid, token)

            val registerUserDto = RegisterUserDto(
                uid = user.uid,
                displayName = user.displayName!!,
                profilePictureUrl = user.photoUrl?.toString()!!,
                device = currentDevice.toDto()
            )

            userApiService.registerUser(registerUserDto)
        }
    }
}