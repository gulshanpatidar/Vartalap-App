package com.example.suruchat_app.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatScreen
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatViewModel
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatViewModelFactory
import com.example.suruchat_app.ui.screens.chat.ChatScreen
import com.example.suruchat_app.ui.screens.full_image.FullImageScreen
import com.example.suruchat_app.ui.screens.home.HomeScreen
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.screens.home.HomeViewModelFactory
import com.example.suruchat_app.ui.screens.login.LoginScreen
import com.example.suruchat_app.ui.screens.profile.ProfileScreen
import com.example.suruchat_app.ui.screens.profile.ProfileViewModel
import com.example.suruchat_app.ui.screens.profile.ProfileViewModelFactory
import com.example.suruchat_app.ui.screens.signup.SignupScreen
import com.example.suruchat_app.ui.screens.splash.SplashScreen
import com.example.suruchat_app.ui.util.ImageHolder
import com.example.suruchat_app.ui.util.Routes

@ExperimentalCoilApi
@Composable
fun AppNavigation(
    navController: NavHostController,
    userPreferences: UserPreferences
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(
            Routes.Home.route
        ) { backStackEntry ->

            val viewModelFactory = HomeViewModelFactory(navController,userPreferences)
            val homeViewModel = viewModel<HomeViewModel>(factory = viewModelFactory)

            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable(Routes.Splash.route) {
            SplashScreen(navController)
        }

        composable(Routes.Login.route) {
            LoginScreen(navController, userPreferences)
        }

        composable(Routes.SignUp.route) {
            SignupScreen(navController)
        }

        composable(Routes.AddNewChat.route) {

            val viewModelFactory = AddNewChatViewModelFactory(navController)
            val addNewChatViewModel = viewModel<AddNewChatViewModel>(factory = viewModelFactory)

            AddNewChatScreen(navController, addNewChatViewModel)
        }

        composable(Routes.Profile.route) {

            val viewModelFactory = ProfileViewModelFactory(userPreferences)
            val profileViewModel = viewModel<ProfileViewModel>(factory = viewModelFactory)
            ProfileScreen(navController = navController, profileViewModel)
        }

        composable(
            Routes.FullImage.route + "/{image}",
            arguments = listOf(
                navArgument("image") {
                    NavType.StringType
                }
            )) { navBackStackEntry ->
            val image = navBackStackEntry.arguments?.getString("image")
            FullImageScreen(navController = navController, image = image)
        }

        composable(
            Routes.Chat.route + "/{chatId}",
            arguments = listOf(
                navArgument(
                    name = "chatId"
                ) {
                    NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            ChatScreen(navController, chatId)
        }
    }
}