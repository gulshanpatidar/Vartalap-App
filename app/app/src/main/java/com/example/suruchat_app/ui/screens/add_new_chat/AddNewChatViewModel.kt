package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.domain.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddNewChatViewModel(
    val navController: NavHostController,
    val isInternetAvailable: Boolean,
    val chatUseCases: ChatUseCases
) : ViewModel() {

    var appUsers: MutableState<List<User>> = mutableStateOf(ArrayList())
    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val errorMessage: MutableState<String> = mutableStateOf("")
    private val service = ChatService.create()

    private var getUsersJob: Job? = null

    init {
//        if (isInternetAvailable){
//            getOfflineThenOnlineUsers()
//        }else{
//            getOfflineUsers()
//        }
        getUsers()
    }

    fun getOfflineUsers() {
        getUsersJob?.cancel()
        getUsersJob = chatUseCases.getUsers()
            .onEach {
                appUsers.value = it
            }
            .launchIn(viewModelScope)
        isLoading.value = false
    }

    fun getOfflineThenOnlineUsers() {
        getUsersJob?.cancel()
        getUsersJob = chatUseCases.getUsers()
            .onEach {
                appUsers.value = it
            }
            .launchIn(viewModelScope)
        isLoading.value = false
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            when (val result = service.getUsers()) {
                is Resource.Success -> {
                    appUsers.value = result.data!!
//                    loadedUsers.forEach {
//                        chatUseCases.addUser(it)
//                    }
                    isLoading.value = false
                }
                is Resource.Error -> {
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                }
                else -> {

                }
            }
        }
    }

    fun searchUser(username: String){
//        appUsers.value.forEach {
//            if ()
//        }
    }

    fun startChat(userId: String) {
        viewModelScope.launch {
//            if (isInternetAvailable){
            service.startChat(userId = userId)
            navController.navigateUp()
//            }
        }
    }
}