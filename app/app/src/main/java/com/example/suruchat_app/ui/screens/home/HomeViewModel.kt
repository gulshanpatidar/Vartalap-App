package com.example.suruchat_app.ui.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.data.remote.dto.UserChat
import com.example.suruchat_app.ui.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(val navController: NavHostController) : ViewModel() {

    var userChats: MutableState<List<UserChat>> = mutableStateOf(ArrayList())
    val service: ChatService = ChatService.create()

    init {
        getMessage()
    }

    fun getMessage() {
        viewModelScope.launch {
            userChats.value = service.getUserChats()
        }
    }

    fun logout(userPreferences: UserPreferences){
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