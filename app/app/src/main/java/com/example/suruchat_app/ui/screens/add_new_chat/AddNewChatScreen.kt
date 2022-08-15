package com.example.suruchat_app.ui.screens.add_new_chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.domain.models.User
import com.example.suruchat_app.ui.components.ScaffoldUse
import kotlinx.coroutines.launch

@ExperimentalCoilApi
@Composable
fun AddNewChatScreen(navController: NavHostController, viewModel: AddNewChatViewModel) {

    ScaffoldUse(
        topBarTitle = "Start new chat",
        topButtonImageVector = Icons.Default.ArrowBack,
        onClickTopButton = { navController.navigateUp() }) {

        var appUsers = viewModel.appUsers.value

        var tempAppUsers by remember {
            viewModel.appUsers
        }

        val isLoading by remember {
            viewModel.isLoading
        }

        val errorMessage by remember {
            viewModel.errorMessage
        }

        var query by remember {
            mutableStateOf("")
        }

        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember {
            SnackbarHostState()
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (errorMessage.isEmpty()) {
                Column {
                    SearchBar(query = query, onQueryFilled = { userQuery ->
                        query = userQuery
                        val list = mutableListOf<User>()
                        appUsers.forEach{
                            if (it.username.startsWith(query)){
                                list.add(it)
                            }
                        }
                        tempAppUsers = list
                    }) {
                        val list = mutableListOf<User>()
                        appUsers.forEach{
                            if (it.username.startsWith(query)){
                                list.add(it)
                            }
                        }
                        tempAppUsers = list
                    }
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(tempAppUsers) {
                            NewUserOption(it) {
                                if (viewModel.isInternetAvailable) {
                                    coroutineScope.launch {
                                        val isSuccessful = viewModel.startChat(it)
                                        if (isSuccessful){
                                            navController.navigateUp()
                                        }
                                    }
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("cannot start chat while offline.")
                                    }
                                }
                            }
                            Divider()
                        }
                    }
                    SnackbarHost(hostState = snackbarHostState)
                }
            } else {
                Text(text = errorMessage, color = MaterialTheme.colors.error)
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
            val painter = rememberImagePainter(data = user.imageurl) {
                transformations(CircleCropTransformation())
            }
            Image(
                painter = painter,
                contentDescription = "Sender Image",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(60.dp)
                    .border(2.dp, Color.Gray, CircleShape)
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
        Column {
            Text(text = user.fullname, fontSize = 24.sp)
            Text(text = user.username, fontSize = 16.sp)
        }
    }
}

@Composable
internal fun SearchBar(
    query: String,
    onQueryFilled: (String) -> Unit,
    onButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.clip(RoundedCornerShape(32.dp))
        ) {
            TextField(
                value = query, onValueChange = {
                    onQueryFilled(it)
                },
                placeholder = {
                    Text(text = "Search with username")
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
        IconButton(
            onClick = {
                onButtonClicked()
            },
            Modifier.padding(start = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search User",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.secondary)
                    .padding(8.dp)
                    .size(45.dp),
                tint = Color.White
            )
        }
    }
}

//@Preview()
//@Composable
//fun PreviewUserOption() {
//    NewUserOption(User("Gulshan Patidar", "1")) {
//
//    }
//}