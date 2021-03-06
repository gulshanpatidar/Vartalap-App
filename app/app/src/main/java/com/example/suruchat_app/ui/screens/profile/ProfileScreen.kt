package com.example.suruchat_app.ui.screens.profile

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.suruchat_app.data.local.GetToken
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.components.ScaffoldUse
import com.example.suruchat_app.ui.util.ImageHolder
import com.example.suruchat_app.ui.util.Routes
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@ExperimentalCoilApi
@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel
) {

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var fullname by remember {
        mutableStateOf(GetToken.USER_NAME)
    }

    val imageString by remember {
        profileViewModel.imageUrl
    }

    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
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

    if (showFullImage) {
        showFullImage = false
        val encodedImage = URLEncoder.encode(GetToken.USER_IMAGE, StandardCharsets.UTF_8.toString())
        navController.navigate(Routes.FullImage.route + "/$encodedImage")
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
                                profileViewModel.uploadImage(fileName, file)
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
            topBarTitle = "Profile",
            topButtonImageVector = Icons.Default.ArrowBack,
            onClickTopButton = { navController.navigateUp() }) {

            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                2.dp, Color.Black,
                                CircleShape
                            )
                            .align(Alignment.CenterHorizontally)
                    ) {
                        if (imageString.isEmpty()) {
                            Image(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Image",
                                modifier = Modifier
                                    .size(200.dp)
                                    .align(CenterHorizontally)
                            )
                        } else {
                            val painter = rememberImagePainter(data = imageString) {
                                transformations(CircleCropTransformation())
                            }
                            Image(
                                painter = painter,
                                contentDescription = "User Image",
                                modifier = Modifier
                                    .size(300.dp)
                                    .clickable {
                                        showFullImage = true
                                    }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Change Profile Picture",
                        Modifier
                            .padding(8.dp)
                            .align(CenterHorizontally)
                            .clickable {
                                if (profileViewModel.isInternetAvailable) {
                                    showImagePicker = true
                                    launcher.launch("image/*")
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("cannot update profile while offline.")
                                    }
                                }
                            },
                        fontSize = 20.sp,
                        color = Color.Blue.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        value = fullname.toString(),
                        onValueChange = {
                            fullname = it
                        },
                        label = {
                            Text(text = "Full name")
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .align(CenterHorizontally)
                    )
                    Button(onClick = {
                        if (fullname.toString().isNotEmpty()) {
                            if (profileViewModel.isInternetAvailable){
                                profileViewModel.updateProfile(fullname.toString())
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Full name updated!")
                                }
                            }else{
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Cannot update profile while offline.")
                                }
                            }
                        }
                    }, modifier = Modifier.align(CenterHorizontally)) {
                        Text(text = "Update Profile")
                    }
                    SnackbarHost(hostState = snackbarHostState)
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun PreviewProfileScreen() {
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.White)) {
//        Spacer(modifier = Modifier.height(10.dp))
//        Row(Modifier.fillMaxWidth()) {
//            Row(modifier = Modifier.weight(0.5f)) {
//                IconButton(
//                    onClick = {
//                    },
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Clear,
//                        contentDescription = "cancel uploading image",
//                        modifier = Modifier.size(60.dp)
//                    )
//                }
//            }
//            Row(modifier = Modifier.weight(0.5f),horizontalArrangement = Arrangement.End) {
//                IconButton(
//                    onClick = {
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = "Confirm uploading image",
//                        modifier = Modifier.size(60.dp)
//                    )
//                }
//            }
//        }
//    }
//}