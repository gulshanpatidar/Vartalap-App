package com.example.suruchat_app.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suruchat_app.data.local.UserPreferences
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

    fun logout(userPreferences: UserPreferences){
        viewModelScope.launch {
            userPreferences.saveUserLoginToken("")
        }
    }
}