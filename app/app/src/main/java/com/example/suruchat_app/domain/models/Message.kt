package com.example.suruchat_app.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.lang.Exception

@Entity(tableName = "message")
@Serializable
data class Message(
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "text") val text: String,
    @PrimaryKey val _id: String
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

class InvalidMessageException(message: String): Exception(message)