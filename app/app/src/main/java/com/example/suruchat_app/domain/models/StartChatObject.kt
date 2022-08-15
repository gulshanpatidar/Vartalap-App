package com.example.suruchat_app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class StartChatObject(
    val userId: String
)

@Serializable
data class StartChatResponse(
    val message: String = ""
)
