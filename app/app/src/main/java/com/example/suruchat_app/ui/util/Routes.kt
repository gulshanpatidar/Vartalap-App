package com.example.suruchat_app.ui.util

sealed class Routes(val route: String){
    object Home: Routes("home_screen")
    object Splash: Routes("splash_screen")
    object Login: Routes("login_screen")
    object SignUp: Routes("signup_screen")
    object AddNewChat: Routes("add_new_chat_screen")
    object Chat: Routes("chat_screen")
    object Profile: Routes("profile_screen")
    object FullImage: Routes("full_image_screen")
}
