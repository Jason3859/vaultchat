package dev.jason.app.compose.vaultchat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _currentRoute = MutableStateFlow<Routes?>(null)
    val currentRoute = _currentRoute.asStateFlow()

    fun onAction(action: Action) {
        when (action) {
            is Action.SignInComplete -> {
                _currentRoute.update { Routes.Messaging }
            }

            is Action.UpdateRoute -> {
                _currentRoute.update { action.route }
            }
        }
    }

    interface Action {
        data object SignInComplete : Action
        data class UpdateRoute(val route: Routes) : Action
    }

    interface Routes {
        data object Auth : Routes
        data object Messaging : Routes
    }
}