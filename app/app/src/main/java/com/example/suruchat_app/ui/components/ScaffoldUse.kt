package com.example.suruchat_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.example.suruchat_app.data.local.UserPreferences
import com.example.suruchat_app.ui.screens.home.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun ScaffoldUse(
    topBarTitle: String,
    topButtonImageVector: ImageVector,
    onClickTopButton: () -> Unit,
    viewModel: HomeViewModel? = null,
    userPreferences: UserPreferences? = null,
    navController: NavHostController? = null,
    fabButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {

    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))

    //use coroutine to open and close drawer
    val coroutineScope = rememberCoroutineScope()

    if (viewModel != null && userPreferences != null && navController != null) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(
                    topBarTitle,
                    {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    },
                    topButtonImageVector
                )
            },
            drawerContent = {
                NavigationDrawer(
                    viewModel = viewModel,
                    userPreferences = userPreferences,
                    navController = navController
                ) {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            },
            floatingActionButton = {
                fabButton()
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                content()
            }
        }
    } else {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopBar(topBarTitle, onClickTopButton, topButtonImageVector)
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                content()
            }
        }
    }
}