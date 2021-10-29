package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserChat(
    val fullname: String,
    val id: String,
    val chatid: String,
    val imageurl: String = ""
)

@Serializable
data class ChatResponse(
    val users: List<UserChat>
)