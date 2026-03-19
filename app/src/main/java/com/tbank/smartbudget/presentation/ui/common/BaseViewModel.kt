package com.tbank.smartbudget.presentation.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbank.smartbudget.data.remote.interceptor.ApiException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Базовая ViewModel для централизованной обработки Result и показа ошибок.
 */
abstract class BaseViewModel : ViewModel() {

    // Единый поток ошибок для подписки на UI слое
    private val _errorFlow = MutableSharedFlow<String>()
    val errorFlow = _errorFlow.asSharedFlow()

    /**
     * Экстеншен для обработки Result<T> в любом ViewModel-наследнике.
     */
    protected fun <T> Result<T>.handle(
        onSuccess: (T) -> Unit,
        onError: ((String) -> Unit)? = null
    ) {
        onSuccess { data ->
            onSuccess(data)
        }
        onFailure { exception ->
            val errorMessage = when (exception) {
                is ApiException -> {
                    "Ошибка ${exception.code}: ${exception.message}"
                }
                else -> exception.localizedMessage ?: "Произошла неизвестная ошибка"
            }

            if (onError != null) {
                onError(errorMessage)
            } else {
                emitError(errorMessage)
            }
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _errorFlow.emit(message)
        }
    }
}