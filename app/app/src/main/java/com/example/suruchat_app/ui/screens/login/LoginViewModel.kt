package com.example.suruchat_app.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch

class LoginViewModel(val navcontroller: NavHostController) : ViewModel() {

    val userId = mutableStateOf("")
    val token = mutableStateOf("")
    val service = ChatService.create()

    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            val response = service.login(username, password)
            userId.value = response.userId
            token.value = response.token
            if (userId.value.isNotEmpty()) {
                navcontroller.navigate(Routes.Home.route){
                    popUpTo(Routes.Login.route){
                        inclusive = true
                    }
                }
            }
        }
    }
}