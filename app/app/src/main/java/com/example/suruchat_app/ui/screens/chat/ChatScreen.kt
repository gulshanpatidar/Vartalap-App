package com.example.suruchat_app.ui.screens.chat

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.domain.models.Message
import com.example.suruchat_app.domain.models.SendMessageObject
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.example.suruchat_app.ui.util.Routes
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoilApi
@ExperimentalMaterialApi
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatId: String,
    userName: String?,
    userImage: String?,
    viewModel: ChatViewModel
) {

    viewModel.getMessagesInit(chatId)

    println("Chat id is - $chatId")
    var messages by remember {
        viewModel.messages
    }

    val localMessages = remember {
        mutableStateListOf<Message>()
    }

    var isLoading by remember {
        viewModel.isLoading
    }

    val errorMessage by remember {
        viewModel.errorMessage
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val isRefreshing by viewModel.isRefreshing.collectAsState()

    //for image uploading
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var fullname by remember {
        mutableStateOf(GetToken.USER_NAME)
    }

    val imageString by remember {
        viewModel.imageUrl
    }

    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val contentResolver = LocalContext.current.contentResolver

    val cacheDir = LocalContext.current.cacheDir

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var showImagePicker by remember {
        mutableStateOf(false)
    }

    var showFullImage by remember {
        mutableStateOf(false)
    }

    if (showImagePicker) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(0.5f)) {
                    IconButton(
                        onClick = {
                            showImagePicker = false
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "cancel uploading image",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
                Row(modifier = Modifier.weight(0.5f), horizontalArrangement = Arrangement.End) {
                    IconButton(
                        onClick = {
                            showImagePicker = false
                            imageUri?.let {
                                contentResolver.query(it, null, null, null, null)
                            }?.use { cursor ->

                                cursor.moveToFirst()
                                val fileNameIndex =
                                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                println("File index is - $fileNameIndex")
                                val fileName = cursor.getString(fileNameIndex)
                                println("File name is - $fileName")

                                val parcelFileDescriptor =
                                    contentResolver.openFileDescriptor(imageUri!!, "r", null)
                                val inputStream =
                                    FileInputStream(parcelFileDescriptor?.fileDescriptor)
                                val file = File(cacheDir, fileName)
                                val outputStream = FileOutputStream(file)
                                inputStream.copyTo(outputStream)
//                                val stream = ByteArrayOutputStream()
//                                bitmap.value?.compress(Bitmap.CompressFormat.PNG,90,stream)
//                                val byteArray = stream.toByteArray()
                                isLoading = true
                                viewModel.uploadImage(chatId,fileName, file)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Confirm uploading image",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)

                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }
            }
        }
    } else {
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

                    SwipeRefresh(
                        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                        onRefresh = { viewModel.refresh(chatId) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(ScrollState(0),reverseScrolling = true),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            SnackbarHost(hostState = snackbarHostState)
                            Spacer(modifier = Modifier.height(10.dp))
                            Column(modifier = Modifier.fillMaxSize()) {
                                messages.forEach {
                                    if (it.senderId == GetToken.USER_ID) {
                                        MessageCardSender(msg = it,navController)
                                        Spacer(modifier = Modifier.height(3.dp))
                                    } else {
                                        MessageCardReceiver(msg = it, userImage!!,navController)
                                        Spacer(modifier = Modifier.height(3.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            var message by remember {
                                mutableStateOf("")
                            }
                            CreateMessage(message = message, onMessageFilled = {
                                message = it
                            }, onImageButtonClicked = {
                                if (viewModel.isInternetAvailable) {
                                    showImagePicker = true
                                    launcher.launch("image/*")
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("cannot send image while offline.")
                                    }
                                }
                            }, onButtonClicked = {
                                if (message.isNotEmpty()) {
                                    if (viewModel.isInternetAvailable) {
                                        localMessages.add(
                                            Message(
                                                _id = "",
                                                senderId = GetToken.USER_ID!!,
                                                createdAt = System.currentTimeMillis(),
                                                text = message
                                            )
                                        )
                                        val messageObject =
                                            SendMessageObject(
                                                chatId!!,
                                                System.currentTimeMillis(),
                                                message,
                                                image = imageString
                                            )
                                        viewModel.sendMessage(messageObject)
                                        message = ""
                                    } else {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Cannot send message while offline.")
                                        }
                                    }
                                }
                            })
                        }
                    }
                } else {
                    Text(text = errorMessage, color = MaterialTheme.colors.error)
                }
            }
        }
    }
}

@Composable
fun CreateMessage(
    message: String,
    onMessageFilled: (String) -> Unit,
    onButtonClicked: () -> Unit,
    onImageButtonClicked: () -> Unit
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
            Row() {
                TextField(
                    value = message, onValueChange = {
                        onMessageFilled(it)
                    },
                    placeholder = {
                        Text(text = "Enter your message")
                    },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    trailingIcon = {
                        IconButton(onClick = {
                            onImageButtonClicked()
                        }) {
                            Icon(
                                imageVector = Icons.Default.InsertPhoto,
                                contentDescription = "send photo"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    )
                )
            }
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
fun MessageCardReceiver(msg: Message, userImage: String, navController: NavHostController) {
    Row(modifier = Modifier.padding(end = 50.dp, start = 8.dp)) {
        Column {
            if (userImage != "image") {
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
            val date = Date(msg.createdAt)
            val formatter = SimpleDateFormat("HH:mm")
            val dateString = formatter.format(date)
            Text(text = dateString, fontSize = 12.sp)
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
            if (msg.text.isNotEmpty()) {
                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                val painter = rememberImagePainter(data = msg.image)
                Image(painter = painter, contentDescription = "Sent Image",
                    Modifier
                        .size(200.dp)
                        .clickable {
                            val encodedImage = URLEncoder.encode(
                                msg.image,
                                StandardCharsets.UTF_8.toString()
                            )
                            navController.navigate(Routes.FullImage.route + "/$encodedImage")
                        })
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
internal fun MessageCardSender(msg: Message, navController: NavHostController) {
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
            if (msg.text.isNotEmpty()) {
                Text(
                    text = msg.text,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                val painter = rememberImagePainter(data = msg.image)
                Log.i("Testing", "MessageCardSender: Image is - ${msg.image}")
                Image(painter = painter, contentDescription = "Sent Image",
                    Modifier
                        .size(200.dp)
                        .clickable {
                            val encodedImage =
                                URLEncoder.encode(msg.image, StandardCharsets.UTF_8.toString())
                            navController.navigate(Routes.FullImage.route + "/$encodedImage")
                        })
            }
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