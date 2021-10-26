package com.example.suruchat_app.ui

import androidx.lifecycle.*
import com.example.suruchat_app.data.local.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(
    val userPreferences: UserPreferences
) : ViewModel() {

    private var _token = MutableLiveData<String>()
    var token: LiveData<String> = _token

    init {
        getUserToken()
    }

    fun getUserToken() {
        viewModelScope.launch {
            token = userPreferences.userLoginToken.asLiveData()
        }
    }
}