package com.example.suruchat_app.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.lang.Exception

@Entity(tableName = "user_chat")
@Serializable
data class UserChat(
    @ColumnInfo(name = "full_name") val fullname: String,
    @PrimaryKey val id: String,
    @ColumnInfo(name = "chat_id") val chatid: String,
    @ColumnInfo(name = "image_url") val imageurl: String = ""
)

@Serializable
data class ChatResponse(
    val users: List<UserChat>
)

class InvalidUserChatException(message: String): Exception(message)