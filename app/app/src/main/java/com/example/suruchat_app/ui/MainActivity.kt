package com.example.suruchat_app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.theme.SuruChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences

    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuruChatAppTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    val context = LocalContext.current
                    userPreferences = UserPreferences(context)
                    val mainViewModel = MainViewModel(userPreferences = userPreferences)
                    var tokenChecked by remember {
                        mutableStateOf(false)
                    }
                    val loginToken by mainViewModel.token.observeAsState()
                    val userId by mainViewModel.userId.observeAsState()
                    val userImage by mainViewModel.userImage.observeAsState()
                    val userName by mainViewModel.userName.observeAsState()

                    if (!tokenChecked){
                        loginToken?.let {
                            if (it.isNotEmpty()){
                                GetToken.ACCESS_TOKEN = loginToken
                                GetToken.USER_ID = userId
                                GetToken.USER_IMAGE = userImage
                                GetToken.USER_NAME = userName
                                println("Token not empty - $it")
                            }
                            tokenChecked = true
                        }
                    }else{
                        AppNavigation(
                            navController = navController,
                            userPreferences = userPreferences
                        )
                    }
                }
            }
        }
    }
}

