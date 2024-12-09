package com.arash.altafi.chatandroid.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arash.altafi.chatandroid.data.model.res.ReceiveGetMessagesModel
import com.arash.altafi.chatandroid.ui.theme.CustomFont
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val chats by chatViewModel.liveGetMessages.observeAsState(emptyList<ReceiveGetMessagesModel>())
    val introduce by chatViewModel.liveGetIntroduce.observeAsState()

    LaunchedEffect(Unit) {
        chatViewModel.getAllMessages()
    }

    LaunchedEffect(Unit) {
        chatViewModel.sendAndReceiveIntroduce("arash")
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//    ) {
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            fontFamily = CustomFont,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = introduce?.toString() ?: "",
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            fontFamily = CustomFont,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(chats.size) { chat ->
                Text(
                    text = chats[chat].message ?: "",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    fontFamily = CustomFont,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
//        }
    }
}