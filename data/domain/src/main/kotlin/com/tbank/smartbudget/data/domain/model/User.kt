package com.tbank.smartbudget.data.domain.model

@JvmInline value class UserId(val value: Long)
data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val token: String? = null
)