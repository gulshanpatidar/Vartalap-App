package com.example.suruchat_app.ui.screens.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.Message
import com.example.suruchat_app.data.remote.dto.SendMessageObject
import kotlinx.coroutines.launch

class ChatViewModel(
    chatId: String
) : ViewModel() {

    var messages: MutableState<List<Message>> = mutableStateOf(ArrayList())
    val service = ChatService.create()

    init {
        getMessages(chatId)
    }

    fun getMessages(chatId: String) {
        viewModelScope.launch {
            messages.value = service.getMessages(chatId)
        }
    }

    fun sendMessage(messageObject: SendMessageObject) {
        viewModelScope.launch {
            service.sendMessage(messageObject)
        }
    }
}