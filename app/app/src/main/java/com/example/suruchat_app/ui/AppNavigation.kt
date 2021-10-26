package com.example.suruchat_app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatScreen
import com.example.suruchat_app.ui.screens.chat.ChatScreen
import com.example.suruchat_app.ui.screens.home.HomeScreen
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.screens.login.LoginScreen
import com.example.suruchat_app.ui.screens.signup.SignupScreen
import com.example.suruchat_app.ui.screens.splash.SplashScreen
import com.example.suruchat_app.ui.util.Routes

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: HomeViewModel,
    userPreferences: UserPreferences
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(
            Routes.Home.route
        ) { backStackEntry ->
            HomeScreen(navController = navController, viewModel = viewModel,userPreferences = userPreferences)
        }

        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController,userPreferences)
        }

        composable(Routes.SignUp.route) {
            SignupScreen(navController)
        }

        composable(Routes.AddNewChat.route){
            AddNewChatScreen(navController)
        }

        composable(Routes.Chat.route){
            ChatScreen(navController)
        }
    }
}