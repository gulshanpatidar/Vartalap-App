package com.example.suruchat_app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.suruchat_app.ui.screens.home.HomeScreen
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.screens.login.LoginScreen
import com.example.suruchat_app.ui.screens.signup.SignupScreen
import com.example.suruchat_app.ui.screens.splash.SplashScreen
import com.example.suruchat_app.ui.util.Routes

@Composable
fun AppNavigation(
    viewModel: HomeViewModel,
    navController: NavHostController,
    innerPadding: PaddingValues
) {

    NavHost(navController = navController, startDestination = Routes.Splash.route) {

        composable(
            Routes.Home.route+"/{userId}/{token}",
            arguments = listOf(
                navArgument("userId") { NavType.StringType },
                navArgument("token") { NavType.StringType }
            )
        ) { backStackEntry->
            val userId = backStackEntry.arguments?.getString("userId")
            val token = backStackEntry.arguments?.getString("token")
            HomeScreen(viewModel = viewModel, navController,userId,token)
        }

        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController)
        }

        composable(Routes.SignUp.route) {
            SignupScreen(navController)
        }
    }
}