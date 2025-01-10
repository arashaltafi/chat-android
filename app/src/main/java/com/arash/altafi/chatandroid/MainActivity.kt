package com.arash.altafi.chatandroid

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.arash.altafi.chatandroid.ui.navigation.AppNavigation
import com.arash.altafi.chatandroid.ui.screens.ManageDataList
import com.arash.altafi.chatandroid.utils.language.LocaleUtils
import com.arash.altafi.chatandroid.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocaleUtils.setLocale(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        mainViewModel.connect()

        enableEdgeToEdge()
        setContent {
//            ManageDataList()
            AppNavigation()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleUtils.setLocale(newBase))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.setLocale(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.disconnect()
    }
}