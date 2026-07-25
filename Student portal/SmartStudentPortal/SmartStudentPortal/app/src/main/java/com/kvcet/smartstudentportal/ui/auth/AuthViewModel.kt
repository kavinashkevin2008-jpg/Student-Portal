package com.kvcet.smartstudentportal.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvcet.smartstudentportal.data.model.UserAccount
import com.kvcet.smartstudentportal.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val account: UserAccount) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error("Enter both email and password")
            return
        }
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = repository.login(email.trim(), password)
            _uiState.value = result.fold(
                onSuccess = { LoginUiState.Success(it) },
                onFailure = { LoginUiState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = LoginUiState.Idle
    }
}
