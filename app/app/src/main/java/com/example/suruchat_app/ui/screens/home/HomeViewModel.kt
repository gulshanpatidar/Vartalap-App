package com.example.suruchat_app.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.models.UserChat
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    val isInternetAvailable: Boolean,
    val navController: NavHostController,
    val userPreferences: UserPreferences,
    val chatUseCases: ChatUseCases
) : ViewModel() {

    var userChats: MutableState<List<UserChat>> = mutableStateOf(ArrayList())
    var isLoading: MutableState<Boolean> = mutableStateOf(true)
    var errorMessage: MutableState<String> = mutableStateOf("")
    private val service: ChatService = ChatService.create()

    private var getUserChatsJob: Job? = null

    init {
        getMessageInit()
    }

    fun getMessageInit() {
        if (isInternetAvailable) {
            getOfflineUserChatsThenOnline()
        } else {
            getOfflineUserChats()
        }
    }

    private fun getOfflineUserChats() {
        getUserChatsJob?.cancel()
        getUserChatsJob = chatUseCases.getUserChats()
            .onEach { chats ->
                userChats.value = chats
            }
            .launchIn(viewModelScope)
        isLoading.value = false
    }

    private fun getOfflineUserChatsThenOnline() {
        getUserChatsJob?.cancel()
        getUserChatsJob = chatUseCases.getUserChats()
            .onEach { chats ->
                userChats.value = chats
            }
            .launchIn(viewModelScope)
        isLoading.value = false
        getUserChats()
    }

    fun getUserChats() {
        viewModelScope.launch {
            when (val result = service.getUserChats()) {
                is Resource.Success -> {
                    val loadedUserChats = result.data!!
                    loadedUserChats.forEach {
                        chatUseCases.addUserChat(it)
                    }
                    isLoading.value = false
                }
                is Resource.Error -> {
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                    if (errorMessage.value == "jwt token expired") {
                        logout()
                    }
                }
                else -> {

                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            GetToken.ACCESS_TOKEN = null
            userPreferences.saveUserLoginToken("")
            navController.navigate(Routes.Login.route) {
                popUpTo(Routes.Home.route) {
                    inclusive = true
                }
            }
        }
    }
}