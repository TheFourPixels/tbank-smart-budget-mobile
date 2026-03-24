package com.tbank.smartbudget.core.network.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ErrorInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()

            throw ApiException(
                code = response.code,
                message = errorBody ?: "Unknown error"
            )
        }

        return response
    }
}