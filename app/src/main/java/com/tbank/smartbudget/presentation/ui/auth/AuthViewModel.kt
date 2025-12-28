package com.tbank.smartbudget.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // НОВЫЙ МЕТОД: Инициализация данных при переходе на экран пароля
    fun initAuthData(email: String, isUserExisting: Boolean, userName: String?) {
        _uiState.update {
            it.copy(
                email = email,
                isUserExisting = isUserExisting,
                userName = userName
            )
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                error = null,
                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )
        }
    }

    fun onEmailSubmit(onSuccess: () -> Unit) {
        val email = _uiState.value.email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = authRepository.checkUserExistence(email)

            _uiState.update { state ->
                val name = result.getOrNull()
                state.copy(
                    isLoading = false,
                    userName = name,
                    isUserExisting = name != null
                )
            }
            onSuccess()
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                error = null,
                isPasswordValid = password.length >= 6
            )
        }
    }

    fun onPinDigitEntered(digit: Char) {
        val currentPin = _uiState.value.pinCode
        if (currentPin.length < 4) {
            val newPin = currentPin + digit
            _uiState.update {
                it.copy(
                    pinCode = newPin,
                    isPinComplete = newPin.length == 4
                )
            }
        }
    }

    fun onPinBackspace() {
        val currentPin = _uiState.value.pinCode
        if (currentPin.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    pinCode = currentPin.dropLast(1),
                    isPinComplete = false
                )
            }
        }
    }

    fun onSubmitPassword(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }

    fun onPinComplete(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)
            _uiState.update { it.copy(isLoading = false) }
            onSuccess()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}