package com.example.suruchat_app.data.db

import com.example.suruchat_app.data.db.daos.MessageDao
import com.example.suruchat_app.data.db.daos.UserDao
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class ChatRepositoryImpl(
    private val userDao: UserDao,
    private val messageDao: MessageDao
    ) : ChatRepository {

    override fun getUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }

    override suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override fun getUserChats(): Flow<List<UserChat>> {
        return userDao.getUserChats()
    }

    override suspend fun insertUserChat(userChat: UserChat) {
        userDao.insertUserChat(userChat)
    }

    override fun getMessages(): Flow<List<Message>> {
        return messageDao.getMessages()
    }

    override suspend fun insertMessage(message: Message) {
        messageDao.insertMessage(message)
    }
}