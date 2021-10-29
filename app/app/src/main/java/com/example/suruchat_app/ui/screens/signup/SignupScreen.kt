package com.example.suruchat_app.ui.screens.signup

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.suruchat_app.R
import com.example.suruchat_app.ui.util.Routes

@Composable
fun SignupScreen(navController: NavHostController) {
    var username by remember {
        mutableStateOf("")
    }

    var fullname by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordVisibility = remember { mutableStateOf(false) }

    val viewModel = SignupViewModel(navController = navController)

    val message by viewModel.response.observeAsState("")

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
                painter = painterResource(R.drawable.suruchat_logo),
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
                text = "Sign Up",
                fontSize = 30.sp,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = message, color = MaterialTheme.colors.error)
            OutlinedTextField(
                value = fullname,
                onValueChange = {
                    fullname = it
                },
                label = {
                    Text(text = "Enter Full Name")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
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
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(text = "Email Address")
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
            Button(
                onClick = {
                    viewModel.doSignup(fullname = fullname,username = username,email =  email,password =  password)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Sign Up", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.padding(20.dp))
            Text(
                text = "Login Instead",
                modifier = Modifier.clickable(onClick = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.SignUp.route) {
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
