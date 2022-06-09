package com.example.suruchat_app.domain.models

import androidx.room.*
import kotlinx.serialization.Serializable
import java.lang.Exception

@Entity(tableName = "message",
foreignKeys = [ForeignKey(
    entity = UserChat::class,
    parentColumns = arrayOf("chat_id"),
    childColumns = arrayOf("chat_id"),
    onDelete = ForeignKey.CASCADE
)])
@Serializable
data class Message(
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "text") var text: String = "",
    @ColumnInfo(name = "chat_id") var chatId: String = "",
    var image: String = ""
)

@Entity
data class UserChatWithMessage(
    @Embedded val userChat: UserChat,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "chat_id"
    )
    val messages: List<Message>
)

@Serializable
data class SendMessageObject(
    val chatId: String,
    val createdAt: Long,
    var text: String,
    val image: String
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