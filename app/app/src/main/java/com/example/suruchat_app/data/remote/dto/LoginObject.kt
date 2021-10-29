package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginObject(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val userId: String,
    val fullname: String,
    val imageurl: String = ""
)