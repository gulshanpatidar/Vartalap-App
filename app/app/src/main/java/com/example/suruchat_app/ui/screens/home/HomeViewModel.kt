package com.example.suruchat_app.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.remote.api.ChatService
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val helloMessage = mutableStateOf("Hello world")
    val service = ChatService.create()

    init {
        getMessage()
    }

    fun getMessage() {
        viewModelScope.launch {
            helloMessage.value = service.getChatResponse()
        }
    }
}