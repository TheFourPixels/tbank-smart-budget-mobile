package com.tbank.smartbudget.data.remote.interceptor

import java.io.IOException

/**
 * Кастомное исключение для сетевых ошибок API.
 * Наследуется от IOException, чтобы корректно обрабатываться сетевыми клиентами и корутинами.
 */
class ApiException(
    val code: Int,
    override val message: String
) : IOException(message)