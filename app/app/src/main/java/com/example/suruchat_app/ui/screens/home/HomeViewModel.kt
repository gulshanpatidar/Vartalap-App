package com.example.suruchat_app.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.UserChat
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var userChats: MutableState<List<UserChat>> = mutableStateOf(ArrayList())
    private val service = ChatService.create()

    init {
        getMessage()
    }

    private fun getMessage() {
        viewModelScope.launch {
            userChats.value = service.getUserChats()
        }
    }

    fun logout(userPreferences: UserPreferences){
        viewModelScope.launch {
            userPreferences.saveUserLoginToken("")
        }
    }
}