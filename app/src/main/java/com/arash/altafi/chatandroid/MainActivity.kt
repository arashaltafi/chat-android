package com.arash.altafi.chatandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.arash.altafi.chatandroid.ui.navigation.AppNavigation
import com.arash.altafi.chatandroid.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatViewModel.connect()

        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}