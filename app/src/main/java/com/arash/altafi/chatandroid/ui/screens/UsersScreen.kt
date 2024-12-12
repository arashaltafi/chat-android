package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.UsersViewModel
import com.arash.altafi.chatandroid.R

@Composable
fun UsersScreen(navController: NavController) {
    val usersViewModel: UsersViewModel = hiltViewModel()
    val liveGetUsers by usersViewModel.liveGetUsers.collectAsState()

    LaunchedEffect(Unit) {
        usersViewModel.getUsers()
    }

    liveGetUsers?.users?.let {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(it.size) { user ->
                Text(
                    text = it[user].name
                )
            }
        }
    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "کاربری یافت نشد",
                fontFamily = CustomFont
            )

            LottieComponent(
                size = DpSize(width = 100.dp, height = 50.dp),
                loop = true,
                lottieFile = R.raw.internet_on
            )
        }
    }
}