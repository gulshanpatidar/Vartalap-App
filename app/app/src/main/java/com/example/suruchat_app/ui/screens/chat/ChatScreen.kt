package com.example.suruchat_app.ui.screens.chat

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
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
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.SendMessageObject
import com.example.suruchat_app.domain.use_cases.ChatUseCases
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalMaterialApi
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatId: String?,
    userName: String?,
    userImage: String?,
    viewModel: ChatViewModel
) {

    viewModel.getMessagesInit()

    println("Chat id is - $chatId")
    var messages by remember {
        viewModel.messages
    }

    val localMessages = remember {
        mutableStateListOf<Message>()
    }

    val isLoading by remember {
        viewModel.isLoading
    }

    val errorMessage by remember {
        viewModel.errorMessage
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    ScaffoldUse(
        topBarTitle = userName.toString(),
        topButtonImageVector = Icons.Default.ArrowBack,
        onClickTopButton = { navController.navigateUp() }) {

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {

            if (errorMessage.isEmpty()) {

                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = isRefreshing), onRefresh = { viewModel.refresh() }) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(ScrollState(0)),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(modifier = Modifier.fillMaxSize()) {
                            messages.forEach {
                                if (it.senderId == GetToken.USER_ID) {
                                    MessageCardSender(msg = it)
                                    Spacer(modifier = Modifier.height(3.dp))
                                } else {
                                    MessageCardReceiver(msg = it,userImage!!)
                                    Spacer(modifier = Modifier.height(3.dp))
                                }
                            }
                            localMessages.forEach {
                                MessageCardSender(msg = it)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        var message by remember {
                            mutableStateOf("")
                        }
                        CreateMessage(message = message, onMessageFilled = {
                            message = it
                        }) {
                            localMessages.add(
                                Message(
                                    _id = "",
                                    senderId = GetToken.USER_ID!!,
                                    createdAt = System.currentTimeMillis(),
                                    text = message
                                )
                            )
                            val messageObject =
                                SendMessageObject(chatId!!, System.currentTimeMillis(), message)
                            viewModel.sendMessage(messageObject)
                            message = ""
                        }
                    }
                }
            } else {
                Text(text = errorMessage, color = MaterialTheme.colors.error)
            }
        }
    }
}

@Composable
fun CreateMessage(message: String, onMessageFilled: (String) -> Unit, onButtonClicked: () -> Unit) {
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
                value = message, onValueChange = {
                    onMessageFilled(it)
                },
                placeholder = {
                    Text(text = "Enter your message")
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
        IconButton(
            onClick = {
                onButtonClicked()
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send Message",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.secondary)
                    .padding(12.dp),
                tint = Color.White
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun MessageCardReceiver(msg: Message, userImage: String) {
    Row(modifier = Modifier.padding(end = 50.dp, start = 8.dp)) {
        Column {
            if (userImage!="image") {
                val painter = rememberImagePainter(data = userImage) {
                    transformations(CircleCropTransformation())
                }
                Image(
                    painter = painter,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(40.dp)
                        .border(2.dp, Color.Gray, CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(40.dp)
                        .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                        .padding(16.dp)
                )
            }
            Text(text = "09:00", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Surface(
            color = Color.Gray.copy(alpha = 0.2f),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 24.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
        ) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MessageCardSender(msg: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 50.dp, end = 8.dp), horizontalArrangement = Arrangement.End
    ) {
        Surface(
            color = MaterialTheme.colors.primary,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.clip(
                RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 0.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            )
        ) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

//@Preview
//@Composable
//fun PreviewMessageCard() {
//    MessageCardReceiver(
//        Message("0", 3254873254,"Hello buddy","ewbewid"),
//        ""
//    )
//}