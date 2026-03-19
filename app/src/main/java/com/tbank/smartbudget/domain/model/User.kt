package com.tbank.smartbudget.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val token: String? = null // Поле для хранения JWT токена
)