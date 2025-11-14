package dev.jason.app.compose.messenger.auth.ui.viewmodel.email_auth

data class EmailAuthUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false
)