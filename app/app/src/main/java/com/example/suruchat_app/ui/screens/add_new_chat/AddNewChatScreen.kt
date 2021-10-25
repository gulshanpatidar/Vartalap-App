package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.remote.dto.User
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.example.suruchat_app.ui.util.Routes

@Composable
fun AddNewChatScreen(navController: NavHostController) {

    ScaffoldUse(
        topBarTitle = "Start new chat",
        topButtonImageVector = Icons.Default.ArrowBack,
        onClickTopButton = { navController.navigateUp() }) {
        val users = remember {
            mutableStateListOf(
                User("Gulshan Patidar", "1"),
                User("Tanish Gupta","2"),
                User("Vishal Kumar","3"),
                User("Suryansh Kumar","4")
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(users){
                NewUserOption(it){
                    navController.navigate(Routes.Chat.route)
                }
                Divider()
            }
        }
    }
}

@Composable
fun NewUserOption(user: User,onClickNewChat: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp).clickable {
                                     onClickNewChat()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Sender Image",
            modifier = Modifier
                .padding(end = 8.dp)
                .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.fillMaxWidth(0.8f)) {
            Text(text = user.username, style = MaterialTheme.typography.body1)
        }
    }
}

@Preview()
@Composable
fun PreviewUserOption() {
    NewUserOption(User("Gulshan Patidar","1")){

    }
}