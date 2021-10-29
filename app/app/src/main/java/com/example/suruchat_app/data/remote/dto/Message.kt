package com.example.suruchat_app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val senderId: String,
    val createdAt: Long,
    val text: String,
    val _id: String
)

@Serializable
data class SendMessageObject(
    val chatId: String,
    val createdAt: Long,
    val text: String
)

@Serializable
data class SendMessageResponse(
    val Message: String
)

@Serializable
data class MessageResponse(
    val messages: List<Message>
)
