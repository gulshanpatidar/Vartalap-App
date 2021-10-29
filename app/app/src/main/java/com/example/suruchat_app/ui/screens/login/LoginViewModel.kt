package com.example.suruchat_app.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginViewModel(
    val navcontroller: NavHostController,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val userId = mutableStateOf("")
    private var _token = MutableLiveData<String>()
    var token : LiveData<String> = _token
    private val service = ChatService.create()

    init {
        getUserToken()
    }

    fun doLogin(username: String, password: String) {
        viewModelScope.launch {
            val response = service.login(username, password)
            userId.value = response.userId
            _token.value = response.token
            if (userId.value.isNotEmpty()) {
                GetToken.ACCESS_TOKEN = _token.value
                GetToken.USER_IMAGE = response.imageurl
                GetToken.USER_ID = response.userId
                GetToken.USER_NAME = response.fullname
                userPreferences.saveUserLoginToken(_token.value!!)
                userPreferences.saveUserId(response.userId)
                userPreferences.saveUserImage(response.imageurl)
                userPreferences.saveUserName(response.fullname)
//                navcontroller.navigate(Routes.Home.route) {
//                    popUpTo(Routes.Login.route) {
//                        inclusive = true
//                    }
//                }
            }
        }
    }

    private fun getUserToken() {
        viewModelScope.launch {
            token = userPreferences.userLoginToken.asLiveData()
        }
    }
}