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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.data.local.GetToken
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

    homeViewModel.getUserChats()

    ScaffoldUse(
        topBarTitle = "SuruChat",
        onClickTopButton = { },
        topButtonImageVector = Icons.Default.Menu,
        navController = navController,
        fabButton = {
            FabButton {
                navController.navigate(Routes.AddNewChat.route)
            }
        }
    ) {

//        homeViewModel.getMessage()

        val userChats by remember {
            homeViewModel.userChats
        }

        val isLoading by remember {
            homeViewModel.isLoading
        }

        val errorMessage by remember {
            homeViewModel.errorMessage
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                if (errorMessage.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn(modifier = Modifier.fillMaxHeight(0.8f)) {
                            items(userChats) {
                                Column {
                                    UserOption(it.fullname, it.imageurl, onUserClicked = {

                                        val image = if (it.imageurl.isNotEmpty()){
                                            URLEncoder.encode(
                                                it.imageurl,
                                                StandardCharsets.US_ASCII.toString())
                                        } else{
                                            "image"
                                        }

                                        val pubkey = URLEncoder.encode(
                                            it.pubkey,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        GetToken.PUBLIC_KEY = it.pubkey
                                        navController.navigate(Routes.Chat.route + "/${it.chatid}/${it.fullname}/${image}")
                                    }, onUserImageClicked = {
                                        val image = URLEncoder.encode(
                                            it.imageurl,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                        navController.navigate(Routes.FullImage.route + "/$image")
                                    })
                                    Divider()
                                }
                            }
                        }
                    }
                } else {
                    Text(text = errorMessage, color = MaterialTheme.colors.error)
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
                contentDescription = "User Image",
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
                contentDescription = "User Image",
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
