package com.example.suruchat_app.ui.screens.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    val isInternetAvailable: Boolean
): ViewModel() {

    var imageUrl: MutableState<String> = mutableStateOf(GetToken.USER_IMAGE!!)
    private val service = ChatService.create()

    fun uploadImage(fileName: String, image: File){
        viewModelScope.launch {
            println("User Image Before - ${GetToken.USER_IMAGE}")
            imageUrl.value = service.uploadImage(fileName,image)
            GetToken.USER_IMAGE = imageUrl.value
            println("User Image After - ${GetToken.USER_IMAGE}")
            userPreferences.saveUserImage(imageUrl.value)
        }
    }

    fun updateProfile(fullname: String){
        viewModelScope.launch {
            service.updateProfile(fullname)
            GetToken.USER_NAME = fullname
            userPreferences.saveUserName(fullname)
        }
    }
}