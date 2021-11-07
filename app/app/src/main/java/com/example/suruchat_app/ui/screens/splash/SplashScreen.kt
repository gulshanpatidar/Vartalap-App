package com.example.suruchat_app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.suruchat_app.R
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.vartalap_logo), contentDescription = "app logo",modifier = Modifier.fillMaxSize())
        LaunchedEffect(key1 = true){
            delay(1000L)
            navController.navigate(Routes.Login.route){
                popUpTo(Routes.Splash.route){
                    inclusive = true
                }
            }
        }
    }
}