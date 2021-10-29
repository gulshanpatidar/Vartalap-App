package com.example.suruchat_app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import java.lang.IllegalArgumentException

class HomeViewModelFactory(private val navController: NavHostController): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(navController = navController) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}