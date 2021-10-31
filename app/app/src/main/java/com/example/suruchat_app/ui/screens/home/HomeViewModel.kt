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
import com.example.suruchat_app.domain.util.Resource
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch

class HomeViewModel(val navController: NavHostController,val userPreferences: UserPreferences) : ViewModel() {

    var userChats: MutableState<List<UserChat>> = mutableStateOf(ArrayList())
    var isLoading: MutableState<Boolean> = mutableStateOf(true)
    var errorMessage: MutableState<String> = mutableStateOf("")
    private val service: ChatService = ChatService.create()

    init {
        getMessage()
    }

    fun getMessage() {
        viewModelScope.launch {
            when(val result = service.getUserChats()){
                is Resource.Success ->{
                    userChats.value = result.data!!
                    isLoading.value = false
                }
                is Resource.Error ->{
                    errorMessage.value = result.message.toString()
                    isLoading.value = false
                }
                else -> {

                }
            }
            if (userChats.value.isEmpty()){
                logout()
            }
        }
    }

    fun logout(){
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