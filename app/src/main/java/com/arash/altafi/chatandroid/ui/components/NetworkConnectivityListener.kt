package com.arash.altafi.chatandroid.ui.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun NetworkConnectivityListener(onConnectionChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    DisposableEffect(Unit) {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onConnectionChanged(true)
            }

            override fun onLost(network: Network) {
                onConnectionChanged(false)
            }
        }

        // Register the network callback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        // Cleanup when the composable leaves composition
        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
