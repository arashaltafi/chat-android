package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val liveEditProfile by profileViewModel.liveEditProfile.collectAsState()
    val liveProfile by profileViewModel.liveProfile.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = liveProfile?.avatar,
            contentDescription = liveProfile?.name,
            modifier = Modifier
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

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = liveProfile?.name + " " + liveProfile?.family,
            fontFamily = CustomFont,
            fontSize = 20.sp,
            color = Color.White
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = liveProfile?.id ?: "",
            fontFamily = CustomFont,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}