package com.example.suruchat_app.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatScreen
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatViewModel
import com.example.suruchat_app.ui.screens.add_new_chat.AddNewChatViewModelFactory
import com.example.suruchat_app.ui.screens.chat.ChatScreen
import com.example.suruchat_app.ui.screens.chat.ChatViewModel
import com.example.suruchat_app.ui.screens.chat.ChatViewModelFactory
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
import com.example.suruchat_app.ui.util.Routes

@ExperimentalMaterialApi
@ExperimentalCoilApi
@Composable
fun AppNavigation(
    isInternetAvailable: Boolean,
    navController: NavHostController,
    userPreferences: UserPreferences,
    chatUseCases: ChatUseCases
) {

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(
            Routes.Home.route
        ) { backStackEntry ->

            val viewModelFactory = HomeViewModelFactory(isInternetAvailable = isInternetAvailable,navController = navController,userPreferences = userPreferences,chatUseCases = chatUseCases)
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
            SignupScreen(navController,userPreferences)
        }

        composable(Routes.AddNewChat.route) {

            val viewModelFactory = AddNewChatViewModelFactory(navController,isInternetAvailable,chatUseCases)
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
            Routes.Chat.route + "/{chatId}/{userName}/{userImage}",
            arguments = listOf(
                navArgument(
                    name = "chatId"
                ) {
                    NavType.StringType
                },
                navArgument(
                    name = "userName"
                ){
                    NavType.StringType
                },
                navArgument(
                    name = "userImage"
                ){
                    NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            val userName = backStackEntry.arguments?.getString("userName")
            val userImage = backStackEntry.arguments?.getString("userImage")
            val viewModelFactory = ChatViewModelFactory(isInternetAvailable = isInternetAvailable,chatUseCases = chatUseCases,chatId!!)
            val chatViewModel = viewModel<ChatViewModel>(factory = viewModelFactory)
            ChatScreen(navController = navController,chatId = chatId,userName = userName,userImage = userImage,chatViewModel)
        }
    }
}