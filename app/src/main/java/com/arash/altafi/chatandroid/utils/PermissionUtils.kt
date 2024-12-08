package com.arash.altafi.chatandroid.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat

object PermissionUtils {

    // Check if permission is already granted
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    // Function to show a toast message for the result
    fun showPermissionStatus(context: Context, permission: String, granted: Boolean) {
        val message = if (granted) "$permission was granted" else "$permission was not granted"
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
