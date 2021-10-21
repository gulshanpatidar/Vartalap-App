package com.example.suruchat_app.ui.screens.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SignupViewModel(
    val navController: NavHostController
) : ViewModel() {

    val response = mutableStateOf("")
    val service = ChatService.create()

    fun doSignup(username: String, email: String, password: String) {
        viewModelScope.launch {
            val message = service.signup(username, email, password)
            response.value = message
            if (response.value.isNotEmpty()) {
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.SignUp.route){
                        inclusive = true
                    }
                }
            }
        }
    }
}