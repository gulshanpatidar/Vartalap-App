package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val users: List<UserChat>
)
