package com.example.suruchat_app.di

import android.content.Context
import com.example.suruchat_app.data.remote.api.ChatService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
}