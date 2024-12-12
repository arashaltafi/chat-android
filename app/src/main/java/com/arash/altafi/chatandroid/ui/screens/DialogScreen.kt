package com.arash.altafi.chatandroid.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesModel
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.AuthViewModel
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel
import com.arash.altafi.chatandroid.viewmodel.DialogViewModel

@Composable
fun DialogScreen(navController: NavController) {
    val context = LocalContext.current

    val authViewModel: AuthViewModel = hiltViewModel()
    val dialogViewModel: DialogViewModel = hiltViewModel()
    val introduce by authViewModel.liveIntroduce.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.sendIntroduce()
    }

    LaunchedEffect(introduce) {
        introduce?.state?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            //call api get dialogs
        }
    }

    Text(
        text = introduce?.state ?: "",
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        fontFamily = CustomFont,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}