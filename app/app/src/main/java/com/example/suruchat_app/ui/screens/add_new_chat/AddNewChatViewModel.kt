package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.User
import com.example.suruchat_app.data.remote.util.Resource
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch

class AddNewChatViewModel(
    val navController: NavHostController
): ViewModel() {

    var appUsers: MutableState<List<User>> = mutableStateOf(ArrayList())
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")
    private val service = ChatService.create()

    init {
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            when(val result = service.getUsers()){
                is Resource.Success->{
                    appUsers.value = result.data!!
                    isLoading.value = false
                }
                is Resource.Error->{
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                }
                else -> {

                }
            }
        }
    }

    fun startChat(userId: String){
        viewModelScope.launch {
            service.startChat(userId = userId)
            navController.navigateUp()
        }
    }
}