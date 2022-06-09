package com.example.suruchat_app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val isInternetAvailable: Boolean,private val navController: NavHostController,private val userPreferences: UserPreferences,private val chatUseCases: ChatUseCases): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(isInternetAvailable = isInternetAvailable,navController = navController,userPreferences,chatUseCases = chatUseCases) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}