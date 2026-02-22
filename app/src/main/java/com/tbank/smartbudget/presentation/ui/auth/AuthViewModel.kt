package com.tbank.smartbudget.presentation.ui.auth

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        val email = savedStateHandle.get<String>("email")
        val isUserExisting = savedStateHandle.get<Boolean>("isExisting")
        val userName = savedStateHandle.get<String>("userName")

        if (email != null && isUserExisting != null) {
            _uiState.update {
                it.copy(
                    email = email,
                    isUserExisting = isUserExisting,
                    userName = userName
                )
            }
        }
    }

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

            if (result.isSuccess) {
                val isExisting = result.getOrDefault(false)

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        userName = null,
                        isUserExisting = isExisting
                    )
                }
                onSuccess()
            } else {
                val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Ошибка проверки пользователя"
                Log.e("AuthViewModel", "checkUserExistence failed: $errorMsg")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMsg
                    )
                }
            }
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
        val email = _uiState.value.email
        val password = _uiState.value.password
        val isUserExisting = _uiState.value.isUserExisting
        val userName = _uiState.value.userName

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            var result = if (isUserExisting) {
                authRepository.login(email, password)
            } else {
                val name = if (!userName.isNullOrEmpty()) userName else email.substringBefore("@")
                authRepository.register(email, password, name)
            }

            // Если регистрация не удалась, потому что email занят, пробуем войти
            if (result.isFailure && !isUserExisting) {
                val errorMsg = result.exceptionOrNull()?.message.orEmpty()
                if (errorMsg.contains("already in use", ignoreCase = true) || errorMsg.contains("400")) {
                    Log.d("AuthViewModel", "Registration failed (user exists), trying login...")
                    result = authRepository.login(email, password)
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message
                )
            }

            if (result.isSuccess) {
                Log.d("AuthViewModel", "Auth success, navigating next")
                onSuccess()
            } else {
                Log.e("AuthViewModel", "Auth failed: ${result.exceptionOrNull()?.message}")
            }
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