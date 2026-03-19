package com.tbank.smartbudget.data.remote.interceptor

import com.tbank.smartbudget.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Перехватчик для добавления заголовка Authorization ко всем запросам.
 */
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // Если у нас есть сохраненный токен, добавляем его в заголовок
        val token = sessionManager.getAuthToken()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}