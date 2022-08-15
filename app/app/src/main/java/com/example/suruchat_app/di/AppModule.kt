package com.example.suruchat_app.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.room.Room
import com.example.suruchat_app.domain.repository.ChatRepository
import com.example.suruchat_app.data.db.ChatRepositoryImpl
import com.example.suruchat_app.data.db.SuruChatDatabase
import com.example.suruchat_app.data.local.UserPreferences
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
    fun provideChatService(): ChatService {
        return ChatService.create()
    }

    @Provides
    @Singleton
    fun provideSuruChatDatabase(app: Application): SuruChatDatabase {
        return Room.databaseBuilder(
            app,
            SuruChatDatabase::class.java,
            SuruChatDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideChatRepository(db: SuruChatDatabase): ChatRepository {
        return ChatRepositoryImpl(userDao = db.userDao, messageDao = db.messageDao)
    }

    @Provides
    @Singleton
    fun provideChatUseCases(repository: ChatRepository): ChatUseCases {
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

    @Provides
    @Singleton
    fun providedUserPreferences(app: Application) = UserPreferences(app)

    @Provides
    @Singleton
    fun providesConnectionStatus(app: Application): Boolean {
        var result = false
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }
}