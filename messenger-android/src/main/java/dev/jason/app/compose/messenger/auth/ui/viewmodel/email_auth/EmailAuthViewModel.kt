package dev.jason.app.compose.messenger.auth.ui.viewmodel.email_auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import dev.jason.app.compose.messenger.auth.data.email.FirebaseEmailAuthentication
import dev.jason.app.compose.messenger.auth.ui.util.SnackbarController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmailAuthViewModel(
    private val firebaseEmailAuthentication: FirebaseEmailAuthentication
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmailAuthUiState())
    val uiState = _uiState.asStateFlow()

    fun updateState(uiState: EmailAuthUiState) {
        _uiState.update { uiState }
    }


    fun signin(): Task<AuthResult?>? {

        checkIfEmailIsValid { return null }
        checkIfEmpty { return null }

        return firebaseEmailAuthentication.signin(_uiState.value.email, _uiState.value.password)
    }

    fun login(): Task<AuthResult?>? {

        checkIfEmailIsValid { return null }
        checkIfEmpty { return null }

        return firebaseEmailAuthentication.login(_uiState.value.email, _uiState.value.password)
    }

    private inline fun checkIfEmailIsValid(onFail: () -> Unit) {
        if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email).matches()) {
            SnackbarController.sendEvent("Email is not valid")
            onFail()
        }
    }

    private inline fun checkIfEmpty(onEmpty: () -> Unit) {
        val uiState = _uiState.value

        if (uiState.email == "" || uiState.password == "") {
            SnackbarController.sendEvent("Fields cannot be empty")
            onEmpty()
        }
    }
}