package com.tbank.smartbudget.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()

            // Бросаем кастомный Exception, который перехватит Result.failure в репозитории
            // или CoroutineExceptionHandler в BaseViewModel
            throw ApiException(
                code = response.code,
                message = errorBody ?: "Unknown error"
            )
        }

        return response
    }
}