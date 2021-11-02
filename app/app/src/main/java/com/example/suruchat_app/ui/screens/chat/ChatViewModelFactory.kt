package com.example.suruchat_app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import java.lang.IllegalArgumentException

class ChatViewModelFactory(private val isInternetAvailable: Boolean,private val chatUseCases: ChatUseCases, private val chatId: String) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)){
            return ChatViewModel(isInternetAvailable = isInternetAvailable,chatUseCases = chatUseCases,chatId = chatId) as T
        }
        throw IllegalArgumentException("unknown viewmodel class")
    }
}