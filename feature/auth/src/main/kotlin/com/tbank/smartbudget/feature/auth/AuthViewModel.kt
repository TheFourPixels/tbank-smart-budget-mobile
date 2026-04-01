package com.tbank.smartbudget.feature.auth

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.network.di.IoDispatcher
import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import com.tbank.smartbudget.feature.auth.AuthEffect.NavigateNext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val uiState: StateFlow<AuthUiState>
        field = MutableStateFlow(AuthUiState())

    private val _effect = Channel<AuthEffect>(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val effect = _effect.receiveAsFlow()


    init {
        restoreFromSavedState()
    }

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is AuthIntent.OnEmailSubmit -> handleEmailSubmit()
            is AuthIntent.OnPasswordChanged -> handlePasswordChanged(intent.password)
            is AuthIntent.OnPasswordSubmit -> handlePasswordSubmit()
            is AuthIntent.OnPinDigitEntered -> handlePinDigit(intent.digit)
            AuthIntent.OnPinBackspace -> handlePinBackspace()
            AuthIntent.ClearError -> uiState.update { it.copy(error = null) }
            is AuthIntent.InitAuthData -> handleInit(intent)
        }
    }

    fun restoreFromSavedState() {
        val email = savedStateHandle.get<String>("email")
        val isExisting = savedStateHandle.get<Boolean>("isExisting")
        val name = savedStateHandle.get<String>("userName")
        if (email != null && isExisting != null) {
            uiState.update { it.copy(email = email, isUserExisting = isExisting, userName = name) }
        }
    }

    private fun handleInit(intent: AuthIntent.InitAuthData) {
        uiState.update {
            it.copy(
                email = intent.email,
                isUserExisting = intent.isUserExisting,
                userName = intent.userName
            )
        }
    }

    private fun handleEmailChanged(email: String) {
        uiState.update {
            it.copy(
                email = email,
                error = null,
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )
        }
    }

    private fun handleEmailSubmit() {
        if (!uiState.value.isEmailValid) return
        viewModelScope.launch(ioDispatcher) {
            uiState.update { it.copy(isLoading = true) }
            when (val result = authRepository.checkUserExistence(uiState.value.email)) {
                is AppResult.Success -> {
                    uiState.update { it.copy(isLoading = false, isUserExisting = result.data) }
                    _effect.send(NavigateNext)
                }

                is AppResult.Error -> uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Ошибка при проверке почты"
                    )
                }

            }
        }
    }

    private fun handlePinBackspace() {
        uiState.update { it.copy(pinCode = it.pinCode.dropLast(1), isPinComplete = false) }
    }

    private fun handlePinDigit(digit: Char) {
        val currentPin = uiState.value.pinCode
        if (currentPin.length < 4) {
            val newPin = currentPin + digit
            uiState.update { it.copy(pinCode = newPin, isPinComplete = newPin.length == 4) }
            if (newPin.length == 4) {
                viewModelScope.launch { _effect.send(NavigateNext) }
            }
        }

    }

    private fun handlePasswordSubmit() {
        if (!uiState.value.isPasswordValid) return
        viewModelScope.launch(ioDispatcher) {
            uiState.update { it.copy(isLoading = true, error = null) }
            val state = uiState.value
            val result = if (state.isUserExisting) {
                authRepository.login(state.email, state.password)
            } else {
                authRepository.register(
                    state.email,
                    state.password,
                    name = state.userName ?: state.email.substringBefore("@")
                )
            }
            when (result) {
                is AppResult.Success<*> -> {
                    uiState.update { it.copy(isLoading = false) }
                    _effect.send(NavigateNext)

                }

                is AppResult.Error -> {
                    uiState.update { it.copy(isLoading = false, error = "Ошибка авторизации") }
                }
            }
        }
    }

    private fun handlePasswordChanged(password: String) {
        uiState.update {
            it.copy(
                password = password,
                error = null,
                isPasswordValid = password.length >= 6
            )
        }
    }
}
