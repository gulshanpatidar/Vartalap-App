package com.example.suruchat_app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.suruchat_app.data.local.UserPreferences
import java.lang.IllegalArgumentException

class ProfileViewModelFactory(private val userPreferences: UserPreferences,private val isInternetAvailable: Boolean) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            return ProfileViewModel(userPreferences,isInternetAvailable) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}