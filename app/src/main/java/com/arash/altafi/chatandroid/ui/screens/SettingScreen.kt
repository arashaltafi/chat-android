package com.arash.altafi.chatandroid.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.R
import com.arash.altafi.chatandroid.ui.components.SettingItem
import com.arash.altafi.chatandroid.ui.navigation.Route
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel
import com.arash.altafi.chatandroid.viewmodel.DataStoreViewModel
import kotlinx.coroutines.delay

@Composable
fun SettingScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = (context as? Activity)

    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val authViewModel: AuthViewModel = hiltViewModel()

    val liveLogout by authViewModel.liveLogout.collectAsState()

    LaunchedEffect(liveLogout) {
        liveLogout?.message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            dataStoreViewModel.clearAll()
            authViewModel.resetLogoutState()
            delay(100)
            navController.navigate(Route.Login) {
                popUpTo("setting") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 10.dp)
            .verticalScroll(scrollState),
    ) {
        SettingItem(
            context = context,
            title = R.string.blocks,
            icon = R.drawable.ic_blocks
        ) {
            navController.navigate(Route.BlockList)
        }

        SettingItem(
            context = context,
            title = R.string.last_seen,
            icon = R.drawable.ic_eye
        ) {

        }

        SettingItem(
            context = context,
            title = R.string.about,
            icon = R.drawable.baseline_supervisor_account_24
        ) {
            navController.navigate(Route.About)
        }

        SettingItem(
            context = context,
            title = R.string.logout,
            icon = R.drawable.ic_logout
        ) {
            authViewModel.sendLogout()
        }
    }

}