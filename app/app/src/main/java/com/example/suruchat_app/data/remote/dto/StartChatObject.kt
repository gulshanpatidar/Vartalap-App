package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StartChatObject(
    val user: String
)

@Serializable
data class StartChatResponse(
    val message: String
)
