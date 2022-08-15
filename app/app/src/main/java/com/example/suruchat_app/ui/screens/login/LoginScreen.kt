package com.example.suruchat_app.ui.screens.login

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.suruchat_app.R
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.util.Routes

@Composable
fun LoginScreen(navController: NavHostController, userPreferences: UserPreferences) {

    var isNavigated by remember {
        mutableStateOf(false)
    }

    if (GetToken.ACCESS_TOKEN!=null && !isNavigated){
        println("Token not empty - ${GetToken.ACCESS_TOKEN}")
        isNavigated = true
        navController.navigate(Routes.Home.route) {
            popUpTo(Routes.Login.route) {
                inclusive = true
            }
        }
    }else{

        var username by remember {
            mutableStateOf("")
        }
        var password by remember {
            mutableStateOf("")
        }
        val passwordVisibility = remember { mutableStateOf(false) }

        val viewModel = hiltViewModel<LoginViewModel>()

        var tokenChecked by remember {
            mutableStateOf(false)
        }

        val loginToken by viewModel.token.observeAsState()
//    val loginToken = viewModel.token.value

        if (!tokenChecked) {
            loginToken?.let {
                if (it.isNotEmpty()) {
                    println("Login token present - $loginToken")
                    tokenChecked = true
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) {
                            inclusive = true
                        }
                    }
                }else{
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White), contentAlignment = Alignment.TopCenter
                        ) {
                            Image(
                                painter = painterResource(R.drawable.vartalap_logo),
                                contentDescription = "app logo"
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.60f)
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                                .background(MaterialTheme.colors.surface)
                                .padding(10.dp)
                                .verticalScroll(ScrollState(0))
                        ) {

                            Text(
                                text = "LOGIN",
                                fontSize = 30.sp,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            OutlinedTextField(
                                value = username,
                                onValueChange = {
                                    username = it
                                },
                                label = {
                                    Text(text = "Username")
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                },
                                label = {
                                    Text(text = "Password")
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                                        Icon(
                                            imageVector = if (passwordVisibility.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "password",
                                            tint = Color.Gray
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                            Spacer(modifier = Modifier.padding(10.dp))
                            Text(text = viewModel.userId.value)
                            Button(
                                onClick = {
                                    viewModel.doLogin(username, password)
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(50.dp)
                            ) {
                                Text(text = "Sign In", fontSize = 20.sp)
                            }

                            Spacer(modifier = Modifier.padding(20.dp))
                            Text(
                                text = "Create An Account",
                                modifier = Modifier.clickable(onClick = {
                                    navController.navigate(Routes.SignUp.route) {
                                        popUpTo(Routes.Login.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                })
                            )
                            Spacer(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }
        }
    }
}

