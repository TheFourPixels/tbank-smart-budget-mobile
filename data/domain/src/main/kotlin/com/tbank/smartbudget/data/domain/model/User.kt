package com.tbank.smartbudget.data.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val token: String? = null
)