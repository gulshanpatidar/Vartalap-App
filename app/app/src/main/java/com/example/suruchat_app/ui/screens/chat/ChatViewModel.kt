package com.example.suruchat_app.ui.screens.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.Message
import com.example.suruchat_app.data.remote.dto.SendMessageObject
import com.example.suruchat_app.data.remote.util.Resource
import kotlinx.coroutines.launch

class ChatViewModel(
    chatId: String
) : ViewModel() {

    var messages: MutableState<List<Message>> = mutableStateOf(ArrayList())
    private val service = ChatService.create()
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")

    init {
        getMessages(chatId)
    }

    private fun getMessages(chatId: String) {
        viewModelScope.launch {
            when(val result = service.getMessages(chatId)){
                is Resource.Success->{
                    messages.value = result.data!!
                    isLoading.value = false
                }
                is Resource.Error->{
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                }
                else ->{

                }
            }
        }
    }

    fun sendMessage(messageObject: SendMessageObject) {
        viewModelScope.launch {
            service.sendMessage(messageObject)
        }
    }
}