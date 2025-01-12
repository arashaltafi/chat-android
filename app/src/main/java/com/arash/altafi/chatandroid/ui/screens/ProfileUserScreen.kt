package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel

@Composable
fun ProfileUserScreen(navController: NavController, id: String) {
    if (id == "") {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("خطایی رخ داده است")
        }
        return
    }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val liveUserInfo by profileViewModel.liveUserInfo.collectAsState()

    LaunchedEffect(id) {
        profileViewModel.getUserPeerInfo(id.toInt())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = liveUserInfo?.peerInfo?.avatar,
                contentDescription = liveUserInfo?.peerInfo?.name,
                modifier = Modifier
                    .zIndex(1f)
                    .size(120.dp)
                    .clip(CircleShape)
                    .shadow(8.dp)
                    .border(
                        1.dp,
                        Color.White,
                        CircleShape
                    ),
                contentScale = ContentScale.Crop,
            )
        }
    }
}