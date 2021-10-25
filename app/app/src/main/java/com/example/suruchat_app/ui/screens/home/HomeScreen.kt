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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.data.remote.dto.User
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
                User("Tanish Gupta","2"),
                User("Vishal Kumar","3"),
                User("Suryansh Kumar","4")
            )
        }

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
                    items(users) {
                        Column {
                            UserOption(it){
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
fun UserOption(user: User,onUserClicked: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable {
            onUserClicked()
        }) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Sender Image",
            modifier = Modifier
                .padding(end = 8.dp)
                .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                .padding(8.dp)
        )
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = user.username, style = MaterialTheme.typography.body1)
            Text(text = "Hello buddy...", style = MaterialTheme.typography.body2)
        }
        Text(text = "09:00")
    }
}

@Composable
fun FabButton(onFabClicked: () -> Unit) {
    FloatingActionButton(onClick = { onFabClicked() },elevation = FloatingActionButtonDefaults.elevation(8.dp)) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}

@Preview
@Composable
fun PreviewUserOption() {
    UserOption(User("Gulshan Patidar","1")){

    }
}