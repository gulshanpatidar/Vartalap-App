package com.example.suruchat_app.ui.screens.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    val userPreferences: UserPreferences
) : ViewModel() {

//    var response = mutableStateOf<String>("")
//        private set
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response
    private val service = ChatService.create()

    suspend fun doSignup(fullname: String,username: String, email: String, password: String) : Boolean{
            _response.value = service.signup(fullname,username, email, password)
        return _response.value == "User Signup Successfully"
    }
}