package com.example.suruchat_app.ui

import androidx.lifecycle.*
import com.example.suruchat_app.data.local.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    val userPreferences: UserPreferences
) : ViewModel() {

    private var _token = MutableLiveData<String>()
    var token: LiveData<String> = _token
    private var _userId = MutableLiveData<String>()
    var userId: LiveData<String> = _userId
    private var _userImage = MutableLiveData<String>()
    var userImage: LiveData<String> = _userImage
    private var _userName = MutableLiveData<String>()
    var userName: LiveData<String> = _userName

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            token = userPreferences.userLoginToken.asLiveData()
            userId = userPreferences.userId.asLiveData()
            userImage = userPreferences.userImage.asLiveData()
            userName = userPreferences.userName.asLiveData()
        }
    }
}