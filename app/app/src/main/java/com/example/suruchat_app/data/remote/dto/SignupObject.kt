package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupObject(
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class SignupResponse(
    val message: String
)