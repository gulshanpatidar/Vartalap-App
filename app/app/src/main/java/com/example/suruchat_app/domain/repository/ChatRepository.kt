package com.example.suruchat_app.domain.repository

import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.models.UserChat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getUsers(): Flow<List<User>>

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    fun getUserChats(): Flow<List<UserChat>>

    suspend fun insertUserChat(userChat: UserChat)

    fun getMessages(): Flow<List<Message>>

    suspend fun insertMessage(message: Message)
}