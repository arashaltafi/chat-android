package com.arash.altafi.chatandroid.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.ui.components.LoadingComponent

@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current

    LoadingComponent()
}