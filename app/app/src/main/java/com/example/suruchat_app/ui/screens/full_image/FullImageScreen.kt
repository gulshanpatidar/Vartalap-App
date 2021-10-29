package com.example.suruchat_app.ui.screens.full_image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.suruchat_app.ui.components.ScaffoldUse

@ExperimentalCoilApi
@Composable
fun FullImageScreen(navController: NavHostController,image: String?) {
    ScaffoldUse(topBarTitle = "Profile Picture", topButtonImageVector = Icons.Default.ArrowBack, onClickTopButton = { navController.navigateUp() }) {
        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
            val painter = rememberImagePainter(data = image)
            Image(painter = painter, contentDescription = "Full size image")
        }
    }
}