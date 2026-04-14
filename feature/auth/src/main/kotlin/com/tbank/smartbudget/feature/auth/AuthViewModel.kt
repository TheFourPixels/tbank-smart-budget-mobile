package com.tbank.smartbudget.feature.auth

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.core.network.di.IoDispatcher
import com.tbank.smartbudget.core.network.remote.AppResult
import com.tbank.smartbudget.core.ui.common.BaseViewModel
import com.tbank.smartbudget.data.domain.repository.AuthRepository
import com.tbank.smartbudget.feature.auth.AuthEffect.NavigateNext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<AuthUiState, AuthIntent, AuthEffect>(AuthUiState()) {

    init {
        restoreFromSavedState()
    }

    override fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is AuthIntent.OnEmailSubmit -> handleEmailSubmit()
            is AuthIntent.OnPasswordChanged -> handlePasswordChanged(intent.password)
            is AuthIntent.OnPasswordSubmit -> handlePasswordSubmit()
            is AuthIntent.OnPinDigitEntered -> handlePinDigit(intent.digit)
            AuthIntent.OnPinBackspace -> handlePinBackspace()
            AuthIntent.ClearError -> updateState { copy(error = null) }
            is AuthIntent.InitAuthData -> handleInit(intent)
        }
    }

    fun restoreFromSavedState() {
        val email = savedStateHandle.get<String>("email")
        val isExisting = savedStateHandle.get<Boolean>("isExisting")
        val name = savedStateHandle.get<String>("userName")
        if (email != null && isExisting != null) {
            updateState { copy(email = email, isUserExisting = isExisting, userName = name) }
        }
    }

    private fun handleInit(intent: AuthIntent.InitAuthData) {
        updateState {
            copy(
                email = intent.email,
                isUserExisting = intent.isUserExisting,
                userName = intent.userName
            )
        }
    }

    private fun handleEmailChanged(email: String) {
        updateState {
            copy(
                email = email,
                error = null,
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            )
        }
    }

    private fun handleEmailSubmit() {
        if (!state.value.isEmailValid) return
        viewModelScope.launch(ioDispatcher) {
            updateState { copy(isLoading = true) }
            when (val result = authRepository.checkUserExistence(state.value.email)) {
                is AppResult.Success -> {
                    updateState { copy(isLoading = false, isUserExisting = result.data) }
                    sendEffect(NavigateNext)
                }

                is AppResult.Error -> updateState {
                    copy(
                        isLoading = false,
                        error = "Ошибка при проверке почты"
                    )
                }

            }
        }
    }

    private fun handlePinBackspace() {
        updateState { copy(pinCode = pinCode.dropLast(1), isPinComplete = false) }
    }

    private fun handlePinDigit(digit: Char) {
        val currentPin = state.value.pinCode
        if (currentPin.length < 4) {
            val newPin = currentPin + digit
            updateState { copy(pinCode = newPin, isPinComplete = newPin.length == 4) }
            if (newPin.length == 4) {
                viewModelScope.launch { sendEffect(NavigateNext) }
            }
        }

    }

    private fun handlePasswordSubmit() {
        if (!state.value.isPasswordValid) return
        viewModelScope.launch(ioDispatcher) {
            updateState { copy(isLoading = true, error = null) }
            val state = state.value
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
                    updateState { copy(isLoading = false) }
                    sendEffect(NavigateNext)

                }

                is AppResult.Error -> {
                    updateState { copy(isLoading = false, error = "Ошибка авторизации") }
                }
            }
        }
    }

    private fun handlePasswordChanged(password: String) {
        updateState {
            copy(
                password = password,
                error = null,
                isPasswordValid = password.length >= 6
            )
        }
    }
}