package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val fullname: String,
    val id: String,
    val imageurl: String = ""
)

@Serializable
data class AppUsers(
    val users: List<User>
)