package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.viewmodel.DataStoreViewModel
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.LottieComponent
import com.arash.altafi.chatandroid.ui.theme.CustomFont

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val token by dataStoreViewModel.cachedToken.observeAsState()

    val versionName =
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1"

    LaunchedEffect(Unit) {
        dataStoreViewModel.getToken()
    }

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(
            if (token != null && token != "") Route.Dialog else Route.Login
        ) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = context.getString(R.string.app_name),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CustomFont,
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                LottieComponent(
                    size = DpSize(width = 200.dp, height = 200.dp),
                    loop = true,
                    lottieFile = R.raw.chat_3
                )
            }

            Text(
                text = versionName,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = CustomFont
            )
        }
    }
}