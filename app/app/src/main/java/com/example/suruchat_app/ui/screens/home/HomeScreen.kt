package com.example.suruchat_app.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.suruchat_app.ui.util.Routes

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavHostController,
    userId: String?,
    token: String?
) {
    val messages = remember {
        mutableListOf(Message(author = "api", viewModel.helloMessage.value))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                navController.navigate(Routes.Login.route) {
                    popUpTo(Routes.Home.route) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Log out")
            }
            LazyColumn(modifier = Modifier.fillMaxHeight(0.9f)) {
                items(messages) {
                    MessageCard(msg = it)
                }
            }
            var message by remember {
                mutableStateOf("")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(value = message, onValueChange = {
                    message = it
                } )
                IconButton(onClick = { messages.add(Message("user",message)) }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "send button")
                }
            }
        }
    }

}

@Composable
fun MessageCard(msg: Message) {
    // We toggle the isExpanded variable when we click on this Column
    Column() {
        Text(
            text = msg.author,
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.subtitle2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 1.dp,
        ) {
            Text(
                text = msg.body,
                modifier = Modifier.padding(all = 4.dp),
                // If the message is expanded, we display all its content
                // otherwise we only display the first line
                maxLines = 1,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        viewModel = HomeViewModel(),
        navController = rememberNavController(),
        userId = "",
        token = ""
    )
}