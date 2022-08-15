package com.example.suruchat_app.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.lang.Exception

@Entity(tableName = "user")
@Serializable
data class User(
    @ColumnInfo(name = "full_name") val fullname: String = "",
    @PrimaryKey val _id: String = "",
    @ColumnInfo(name = "image_url") val imageurl: String = "",
    @ColumnInfo(name = "user_name") val username: String = ""
)

@Serializable
data class AppUsers(
    val users: List<User>
)

class InvalidUserException(message: String): Exception(message)