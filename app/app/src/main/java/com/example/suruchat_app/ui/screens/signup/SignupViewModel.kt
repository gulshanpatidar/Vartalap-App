package com.example.suruchat_app.ui.screens.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch


class SignupViewModel(
    val navController: NavHostController
) : ViewModel() {

//    var response = mutableStateOf<String>("")
//        private set
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    private val service = ChatService.create()

    fun doSignup(username: String, email: String, password: String) {
        viewModelScope.launch {
            _response.value = service.signup(username, email, password)
            if (_response.value == "User Signup Successfully") {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.SignUp.route) {
                        inclusive = true
                    }
                }
            }
        }
    }
}