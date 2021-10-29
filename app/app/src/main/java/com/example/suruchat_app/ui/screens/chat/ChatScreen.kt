package com.example.suruchat_app.ui.screens.chat

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.remote.dto.Message
import com.example.suruchat_app.data.remote.dto.SendMessageObject
import com.example.suruchat_app.ui.components.ScaffoldUse

@Composable
fun ChatScreen(navController: NavHostController, chatId: String?) {

    val viewModel = ChatViewModel(chatId!!)
    var messages by remember {
        viewModel.messages
    }

    val localMessages = remember {
        mutableStateListOf<Message>()
    }

    ScaffoldUse(
        topBarTitle = "Chats",
        topButtonImageVector = Icons.Default.ArrowBack,
        onClickTopButton = { navController.navigateUp() }) {
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
                        MessageCardReceiver(msg = it)
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
                localMessages.forEach{
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
                localMessages.add(Message(GetToken.USER_ID.toString(),System.currentTimeMillis(),message,""))
                val messageObject = SendMessageObject(chatId, System.currentTimeMillis(), message)
                viewModel.sendMessage(messageObject)
                message = ""
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

@Composable
fun MessageCardReceiver(msg: Message) {
    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
        Column {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Sender Image",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(width = 2.dp, color = Color.Gray, shape = CircleShape)
                    .padding(8.dp)
            )
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
                .fillMaxWidth(0.8f)
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
//        Message("Gulshan", "Hello World")
//    )
//}