package dev.jason.app.compose.messenger_app.auth_ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.jason.app.compose.messenger_app.auth.Authentication
import dev.jason.app.compose.messenger_app.auth_ui.action.AuthAction
import dev.jason.app.compose.messenger_app.auth_ui.controller.NavigationController
import dev.jason.app.compose.messenger_app.auth_ui.controller.SnackbarController
import dev.jason.app.compose.messenger_app.auth_ui.route.AuthRoute
import dev.jason.app.compose.messenger_app.domain.AuthResult
import dev.jason.app.compose.messenger_app.domain.LocalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class AuthViewModel(
    private val authentication: Authentication,
    private val localStorage: LocalStorage
) : ViewModel(CoroutineScope(Dispatchers.IO)) {

    companion object {
        lateinit var onDone: () -> Unit
    }

    data class UiState(
        val username: String = "",
        val password: String = "",
        val conformPassword: String = "",
        val showPassword: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateState(uiState: UiState) {
        _uiState.update { uiState }
    }

    fun onAction(action: AuthAction) {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Performing action $action")
            when (action) {
                AuthAction.LoginAction -> login()
                AuthAction.SigninAction -> signin()
                AuthAction.SaveUserCredentials -> storeUserCredentialsLocally()
                AuthAction.Done -> {
                    viewModelScope.launch(Dispatchers.Main.immediate) {
                        onDone.invoke()
                    }
                }
            }
        }
    }

    private suspend fun login() {
        checkIfEmpty { return }

        NavigationController.navigate(AuthRoute.LoadingScreen("Logging in"), true)

        authentication.login(
            username = _uiState.value.username,
            password = _uiState.value.password
        ).apply {
            _uiState.update {
                if (this@apply is AuthResult.Success) {
                    onAction(AuthAction.SaveUserCredentials)
                    onAction(AuthAction.Done)
                    return
                }

                if (this@apply is AuthResult.InvalidPassword) {
                    NavigationController.navigate(AuthRoute.LoginScreen, true)
                    SnackbarController.sendWarningDelayed(this@apply)
                    return
                }

                if (this@apply is AuthResult.NotFound) {
                    NavigationController.navigate(AuthRoute.LoginScreen, true)
                    SnackbarController.sendWarningDelayed(this@apply)
                    return
                }

                throw IllegalStateException()
            }
        }
    }

    private suspend fun signin() {
        checkIfEmpty { return }
        checkPassword { return }

        NavigationController.navigate(AuthRoute.LoadingScreen("Creating your account"), true)

        authentication.signin(
            username = _uiState.value.username,
            password = _uiState.value.password
        ).apply {
            _uiState.update {
                if (this@apply is AuthResult.Success) {
                    login()
                    return
                }

                if (this@apply is AuthResult.UserAlreadyExists) {
                    NavigationController.navigate(AuthRoute.SigninScreen, true)
                    SnackbarController.sendWarningDelayed(this@apply)
                    return
                }

                throw IllegalStateException()
            }
        }
    }

    private suspend fun storeUserCredentialsLocally() {
        localStorage.addUser(
            _uiState.value.username,
            _uiState.value.password
        )
    }

    private suspend inline fun checkIfEmpty(onEmpty: () -> Unit) {
        val uiState = _uiState.value

        if (uiState.username == "" || uiState.password == "") {
            SnackbarController.sendFieldsCannotBeEmpty()
            onEmpty()
        }
    }

    private suspend inline fun checkPassword(onFail: () -> Unit) {
        val uiState = _uiState.value

        if (uiState.password != uiState.conformPassword) {
            SnackbarController.sendPasswordsDidNotMatch()
            onFail()
        }
    }

    init {
        viewModelScope.launch {
            val savedUser = localStorage.getUser().first()

            if (savedUser == null) return@launch

            _uiState.update {
                it.copy(
                    username = savedUser.username,
                    password = savedUser.password
                )
            }

            login()
        }
    }
}