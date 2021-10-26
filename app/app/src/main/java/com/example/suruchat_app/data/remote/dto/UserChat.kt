package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserChat(
    val username: String,
    val id: String,
    val chatid: String
)
