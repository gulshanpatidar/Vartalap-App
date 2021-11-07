package com.example.suruchat_app.ui

import android.accounts.NetworkErrorException
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.api.ChatService
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.theme.SuruChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var userPreferences: UserPreferences
    @Inject lateinit var chatUseCases: ChatUseCases
    var isInternetAvailable : Boolean = false

    @ExperimentalMaterialApi
    @ExperimentalCoilApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuruChatAppTheme(darkTheme = false) {

                val navController = rememberNavController()
                val context = LocalContext.current
                userPreferences = UserPreferences(context)
                //check for internet connectivity
                val connectivityManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (
                    activeNetworkInfo != null && activeNetworkInfo.isConnected
                ) {
                    isInternetAvailable = true
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        val mainViewModel = MainViewModel(userPreferences = userPreferences)
                        var tokenChecked by remember {
                            mutableStateOf(false)
                        }
                        val loginToken by mainViewModel.token.observeAsState()
                        val userId by mainViewModel.userId.observeAsState()
                        val userImage by mainViewModel.userImage.observeAsState()
                        val userName by mainViewModel.userName.observeAsState()
                        val privateKey by mainViewModel.privateKey.observeAsState()
                        val loginTime by mainViewModel.loginTime.observeAsState()

                        if (!tokenChecked) {
                            loginToken?.let {
                                if (it.isNotEmpty()) {
                                    GetToken.ACCESS_TOKEN = loginToken
                                    GetToken.USER_ID = userId
                                    GetToken.USER_IMAGE = userImage
                                    GetToken.USER_NAME = userName
                                    GetToken.PRIVATE_KEY = privateKey
                                    GetToken.LOGIN_TIME = loginTime
                                    println("Token not empty - $it and private key - $privateKey")
                                }
                                tokenChecked = true
                            }
                        } else {
                            AppNavigation(
                                isInternetAvailable = isInternetAvailable,
                                navController = navController,
                                userPreferences = userPreferences,
                                chatUseCases = chatUseCases
                            )
                        }
                    }
                } else {
                    isInternetAvailable = false

                    val mainViewModel = MainViewModel(userPreferences = userPreferences)
                    var tokenChecked by remember {
                        mutableStateOf(false)
                    }

                    val loginToken by mainViewModel.token.observeAsState()
                    val userId by mainViewModel.userId.observeAsState()
                    val userImage by mainViewModel.userImage.observeAsState()
                    val userName by mainViewModel.userName.observeAsState()
                    val privateKey by mainViewModel.privateKey.observeAsState()
                    val loginTime by mainViewModel.loginTime.observeAsState()

                    if (!tokenChecked) {
                        loginToken?.let {
                            if (it.isNotEmpty()) {
                                GetToken.ACCESS_TOKEN = loginToken
                                GetToken.USER_ID = userId
                                GetToken.USER_IMAGE = userImage
                                GetToken.USER_NAME = userName
                                GetToken.PRIVATE_KEY = privateKey
                                GetToken.LOGIN_TIME = loginTime
                                println("Token not empty - $it and private key - $privateKey")
                            }
                            tokenChecked = true
                        }
                    } else {
                        AppNavigation(
                            isInternetAvailable = isInternetAvailable,
                            navController = navController,
                            userPreferences = userPreferences,
                            chatUseCases = chatUseCases
                        )
                    }
//                    NetworkError()
                }

            }
        }
    }
}

@Composable
fun NetworkError() {
    Box(modifier = Modifier.background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Filled.Error,
                contentDescription = "Error image",
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "No internet connection",
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
        }
    }
}

