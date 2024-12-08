package com.arash.altafi.chatandroid.ui.components

import android.app.Activity
import androidx.activity.compose.BackHandler
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BackPressHandler(
    navController: NavController,
    onNavigationItemSelected: (Int) -> Unit
) {
    var doubleBackToExitPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = (context as? Activity)
    val coroutineScope = rememberCoroutineScope()

    // Use BackHandler to intercept the system back press
    BackHandler {
        if (navController.previousBackStackEntry != null) {
            // Pop the backstack if there is a previous route
            navController.popBackStack()
            onNavigationItemSelected(0)
        } else {
            // Handle double back press to exit the app
            if (doubleBackToExitPressedOnce) {
                // Exit the app if back is pressed twice within 5 seconds
                activity?.finish()
            } else {
                // Show the toast message and start a 5-second timer
                doubleBackToExitPressedOnce = true
                Toast.makeText(
                    context,
                    "برای خروج یک بار دیگر دکمه برگشت را بزنید",
                    Toast.LENGTH_SHORT
                ).show()

                // Reset the flag after 5 seconds using coroutine
                coroutineScope.launch {
                    delay(5000)  // 5-second delay
                    doubleBackToExitPressedOnce = false
                }
            }
        }
    }
}
