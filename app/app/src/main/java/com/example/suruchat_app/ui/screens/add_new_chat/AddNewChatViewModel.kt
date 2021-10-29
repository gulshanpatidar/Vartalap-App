package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.User
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch

class AddNewChatViewModel(
    val navController: NavHostController
): ViewModel() {

    var appUsers: MutableState<List<User>> = mutableStateOf(ArrayList())
    val service = ChatService.create()

    init {
        getUsers()
    }

    fun getUsers(){
        viewModelScope.launch {
            appUsers.value = service.getUsers()
        }
    }

    fun startChat(userId: String){
        viewModelScope.launch {
            service.startChat(userId = userId)
            navController.navigateUp()
        }
    }
}