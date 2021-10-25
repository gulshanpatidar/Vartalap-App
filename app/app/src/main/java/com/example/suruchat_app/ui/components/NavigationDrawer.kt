package com.example.suruchat_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import com.example.suruchat_app.ui.util.Constants
import com.example.suruchat_app.ui.util.Routes

@Composable
fun NavigationDrawer(
    viewModel: HomeViewModel,
    userPreferences: UserPreferences,
    closeDrawer: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "SuruChat",
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .background(MaterialTheme.colors.primary),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        Divider(Modifier.height(1.dp))
        Text(
            text = "Log out",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.logout(userPreferences)
                    closeDrawer()
                }
                .padding(12.dp),
            fontSize = 20.sp
        )
        Divider(modifier = Modifier.height(1.dp))
    }
}