package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import java.lang.IllegalArgumentException

class AddNewChatViewModelFactory(private val navController: NavHostController): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewChatViewModel::class.java)){
            return AddNewChatViewModel(navController = navController) as T
        }
        throw IllegalArgumentException("unknown viewModel class")
    }
}