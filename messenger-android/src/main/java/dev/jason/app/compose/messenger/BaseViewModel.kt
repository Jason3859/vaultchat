package dev.jason.app.compose.messenger

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BaseViewModel : ViewModel() {

    private val _startDestination = MutableStateFlow<AppRoute>(AppRoute.WebSocketConnection)
    val startDestination = _startDestination.asStateFlow()

    init {
        val user = Firebase.auth.currentUser

        if (user == null) {
            Log.d("BaseViewModel", "init: not logged in")
            _startDestination.update { AppRoute.Authentication }
        } else {
            Log.d("BaseViewModel", "init: already logged in")
        }
    }
}