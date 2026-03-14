package dev.jason.app.compose.vaultchat.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.messaging.messaging
import dev.jason.app.compose.vaultchat.auth.data.RemoteApi
import dev.jason.app.compose.vaultchat.auth.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(private val remoteApi: RemoteApi) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    private val _currentRoute = MutableStateFlow<Route?>(null)
    val currentRoute = _currentRoute.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.SignInComplete -> {
                viewModelScope.launch {
                    _currentRoute.update { null }

                    val uid = Firebase.auth.currentUser!!.uid
                    val fcmToken = Firebase.messaging.token.await()
                    val user = User(uid, fcmToken)

                    remoteApi.addUserToServer(user)

                    _currentRoute.update { Route.Messaging }
                }
            }

            is Action.UpdateRoute -> {
                _currentRoute.update { action.route }
            }
        }
    }

    interface Action {
        data object SignInComplete : Action
        data class UpdateRoute(val route: Route) : Action
    }

    interface Route {
        data object Auth : Route
        data object Messaging : Route
    }
}