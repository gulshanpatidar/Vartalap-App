package com.example.suruchat_app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.dto.User
import com.example.suruchat_app.data.remote.dto.UserChat
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.example.suruchat_app.ui.util.Routes

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavHostController,
    userPreferences: UserPreferences
) {

    ScaffoldUse(
        topBarTitle = "SuruChat",
        onClickTopButton = { },
        topButtonImageVector = Icons.Default.Menu,
        viewModel = viewModel,
        userPreferences = userPreferences,
        navController = navController,
        fabButton = {
            FabButton {
                navController.navigate(Routes.AddNewChat.route)
            }
        }
    ) {
        val users = remember {
            mutableStateListOf(
                User("Gulshan Patidar", "1"),
                User("Tanish Gupta", "2"),
                User("Vishal Kumar", "3"),
                User("Suryansh Kumar", "4")
            )
        }

        val userChats = viewModel.userChats.value

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.fillMaxHeight(0.8f)) {
                    items(userChats) {
                        Column {
                            UserOption(it.username) {
                                navController.navigate(Routes.Chat.route)
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserOption(username: String, onUserClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUserClicked()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "Sender Image",
            modifier = Modifier
                .padding(end = 16.dp)
                .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                .padding(16.dp)
        )
        Text(text = username, fontSize = 24.sp)
    }
}

@Composable
fun FabButton(onFabClicked: () -> Unit) {
    FloatingActionButton(
        onClick = { onFabClicked() },
        elevation = FloatingActionButtonDefaults.elevation(8.dp),
        modifier = Modifier.size(65.dp)
    ) {
        Icon(imageVector = Icons.Default.Chat, contentDescription = "",modifier = Modifier.size(40.dp))
    }
}

@Preview
@Composable
fun PreviewUserOption() {
    UserOption("Gulshan Patidar") {

    }
}