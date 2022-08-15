package com.example.suruchat_app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginObject(
    val username: String,
    val password: String,
    val pubkey: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String,
    val fullname: String,
    val imageurl: String = "",
    val message: String = ""
)