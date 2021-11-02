package com.example.suruchat_app.domain.repository

import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.models.UserChatWithMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getUsers(): Flow<List<User>>

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    fun getUserChats(): Flow<List<UserChat>>

    suspend fun insertUserChat(userChat: UserChat)

    suspend fun insertMessage(message: Message)

    fun getUserChatWithMessages(): Flow<List<UserChatWithMessage>>

    suspend fun getMessagesByChatId(chatId: String): UserChatWithMessage
}