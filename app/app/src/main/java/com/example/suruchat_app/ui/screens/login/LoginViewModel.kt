package com.example.suruchat_app.ui.screens.login

import android.util.Base64
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.security.Curve25519Impl
import com.example.suruchat_app.ui.util.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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

            val keyPair = Curve25519Impl.getKeyPair()

            val publicKeyString = Base64.encodeToString(keyPair.publicKey, Base64.DEFAULT)
            val privateKeyString = Base64.encodeToString(keyPair.privateKey, Base64.DEFAULT)

            GetToken.PRIVATE_KEY = privateKeyString
            userPreferences.savePrivateKey(privateKeyString)

            val response = service.login(username, password,publicKeyString)
            userId.value = response.userId
            _token.value = response.token
            if (userId.value.isNotEmpty()) {
                GetToken.ACCESS_TOKEN = _token.value
                GetToken.USER_IMAGE = response.imageurl
                GetToken.USER_ID = response.userId
                GetToken.USER_NAME = response.fullname
                GetToken.LOGIN_TIME = System.currentTimeMillis()
                userPreferences.saveUserLoginToken(_token.value!!)
                userPreferences.saveUserId(response.userId)
                userPreferences.saveUserImage(response.imageurl)
                userPreferences.saveUserName(response.fullname)
                userPreferences.saveLoginTime(GetToken.LOGIN_TIME!!)
            }
        }
    }

    private fun getUserToken() {
        viewModelScope.launch {
            token = userPreferences.userLoginToken.asLiveData()
        }
    }
}