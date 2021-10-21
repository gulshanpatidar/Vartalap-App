package com.example.suruchat_app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    navController: NavHostController,
    viewModel: HomeViewModel,
    innerPadding: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable(
            Routes.Home.route
        ) { backStackEntry ->
            HomeScreen(navController = navController, viewModel = viewModel)
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