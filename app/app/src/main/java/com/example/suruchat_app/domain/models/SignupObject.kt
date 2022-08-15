package com.example.suruchat_app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SignupObject(
    val fullname: String,
    val username: String,
    val email: String,
    val password: String
)

@Serializable
data class SignupResponse(
    val message: String = ""
)