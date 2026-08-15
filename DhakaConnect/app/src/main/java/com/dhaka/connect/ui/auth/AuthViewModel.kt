package com.dhaka.connect.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhaka.connect.data.repository.AuthResult
import com.dhaka.connect.data.repository.UserRepository
import com.dhaka.connect.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(phone: String, password: String, onSuccess: () -> Unit) {
        if (phone.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your phone number and password.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = userRepository.login(phone, password)) {
                is AuthResult.Success -> {
                    sessionManager.setCurrentUser(result.user.id)
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun register(fullName: String, phone: String, password: String, area: String, onSuccess: () -> Unit) {
        if (fullName.isBlank() || phone.isBlank() || password.length < 4) {
            _uiState.update { it.copy(errorMessage = "Please fill in your name, phone number and a password of at least 4 characters.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = userRepository.register(fullName, phone, password, area)) {
                is AuthResult.Success -> {
                    sessionManager.setCurrentUser(result.user.id)
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
