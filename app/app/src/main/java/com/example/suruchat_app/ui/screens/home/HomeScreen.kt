package com.example.suruchat_app.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.example.suruchat_app.ui.util.Routes
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExperimentalCoilApi
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel
) {

    ScaffoldUse(
        topBarTitle = "SuruChat",
        onClickTopButton = { },
        topButtonImageVector = Icons.Default.Menu,
        viewModel = homeViewModel,
        navController = navController,
        fabButton = {
            FabButton {
                navController.navigate(Routes.AddNewChat.route)
            }
        }
    ) {

        homeViewModel.getMessage()

        val userChats by remember {
            homeViewModel.userChats
        }

        if (userChats.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Start a new chat by clicking button below.",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
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
                                UserOption(it.fullname, it.imageurl, onUserClicked = {
                                    val image = URLEncoder.encode(it.imageurl,StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.Chat.route + "/${it.chatid}/${it.fullname}/${image}")
                                }, onUserImageClicked = {
                                    val image = URLEncoder.encode(it.imageurl,StandardCharsets.UTF_8.toString())
                                    navController.navigate(Routes.FullImage.route + "/$image")
                                })
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun UserOption(
    username: String,
    userImage: String,
    onUserClicked: () -> Unit,
    onUserImageClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onUserClicked()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (userImage.isNotEmpty()) {
            val painter = rememberImagePainter(data = userImage) {
                transformations(CircleCropTransformation())
            }
            Image(
                painter = painter,
                contentDescription = "Sender Image",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(60.dp)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clickable {
                        onUserImageClicked()
                    }
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
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "",
            modifier = Modifier.size(40.dp)
        )
    }
}

//@Preview
//@Composable
//fun PreviewUserOption() {
//    UserOption("Gulshan Patidar") {
//
//    }
//}