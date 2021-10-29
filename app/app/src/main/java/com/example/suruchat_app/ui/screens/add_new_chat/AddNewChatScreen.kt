package com.example.suruchat_app.ui.screens.add_new_chat

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.data.remote.dto.User
import com.example.suruchat_app.ui.components.ScaffoldUse

@Composable
fun AddNewChatScreen(navController: NavHostController, viewModel: AddNewChatViewModel) {

    ScaffoldUse(
        topBarTitle = "Start new chat",
        topButtonImageVector = Icons.Default.ArrowBack,
        onClickTopButton = { navController.navigateUp() }) {
        val users = remember {
            mutableStateListOf(
                User("Gulshan Patidar", "1"),
                User("Tanish Gupta", "2"),
                User("Vishal Kumar", "3"),
                User("Suryansh Kumar", "4")
            )
        }

        val appUsers by remember {
            viewModel.appUsers
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(appUsers) {
                NewUserOption(it) {
                    viewModel.startChat(it.id)
                }
                Divider()
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun NewUserOption(user: User, onClickNewChat: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClickNewChat()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (user.imageurl.isNotEmpty()) {
            val painter = rememberImagePainter(data = user.imageurl){
                transformations(CircleCropTransformation())
            }
            Image(
                painter = painter,
                contentDescription = "Sender Image",
                modifier = Modifier.padding(end = 16.dp).size(60.dp).border(2.dp,Color.Gray, CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Sender Image",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(60.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                    .padding(16.dp)
            )
        }
        Text(text = user.fullname, fontSize = 24.sp)
    }
}

//@Preview()
//@Composable
//fun PreviewUserOption() {
//    NewUserOption(User("Gulshan Patidar", "1")) {
//
//    }
//}