package com.arash.altafi.chatandroid.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.viewmodel.DataStoreViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val token by dataStoreViewModel.cachedToken.observeAsState()

    LaunchedEffect(Unit) {
        dataStoreViewModel.getToken()
    }

    Log.i("test123321", "token: $token")

    val routeNavigate = token?.let {
        "dialog"
    } ?: run {
        "login"
    }

    LaunchedEffect(Unit) {
        delay(500)
        navController.navigate(routeNavigate) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Splash Screen",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }
}