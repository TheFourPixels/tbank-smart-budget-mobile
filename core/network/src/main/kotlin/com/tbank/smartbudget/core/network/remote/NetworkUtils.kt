package com.tbank.smartbudget.core.network.remote

import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

sealed interface AppError {
    data class Network(val code: Int, val message: String) : AppError
    data object NoInternet : AppError
    data class Unknown(val message: String) : AppError
}

sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}

/**
 * Безопасный вызов API с обработкой исключений.
 * Возвращает Result с данными или ошибкой.
 */
suspend inline fun <T> safeApiCall(
    crossinline call: suspend () -> T
): AppResult<T> {
    return try {
        AppResult.Success(call())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        AppResult.Error(AppError.Network(e.code(), e.message()))
    } catch (e: IOException) {
        AppResult.Error(AppError.NoInternet)
    } catch (e: Exception) {
        AppResult.Error(AppError.Unknown(e.localizedMessage ?: "Unknown Error"))
    }
}