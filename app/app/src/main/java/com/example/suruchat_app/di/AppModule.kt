package com.example.suruchat_app.di

import android.app.Application
import androidx.room.Room
import com.example.suruchat_app.domain.repository.ChatRepository
import com.example.suruchat_app.data.db.ChatRepositoryImpl
import com.example.suruchat_app.data.db.SuruChatDatabase
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideChatService(): ChatService{
        return ChatService.create()
    }

    @Provides
    @Singleton
    fun provideSuruChatDatabase(app: Application): SuruChatDatabase{
        return Room.databaseBuilder(
            app,
            SuruChatDatabase::class.java,
            SuruChatDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideChatRepository(db: SuruChatDatabase): ChatRepository {
        return ChatRepositoryImpl(userDao = db.userDao,messageDao = db.messageDao)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases{
        return ChatUseCases(
            addUser = AddUser(repository = repository),
            getUsers = GetUsers(repository),
            deleteUser = DeleteUser(repository),
            addUserChat = AddUserChat(repository),
            getUserChats = GetUserChats(repository),
            addMessage = AddMessage(repository),
            getMessages = GetMessages(repository)
        )
    }
}