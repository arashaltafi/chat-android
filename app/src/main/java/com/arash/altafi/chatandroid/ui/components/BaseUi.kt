package com.arash.altafi.chatandroid.ui.components

import androidx.compose.runtime.Composable

@Composable
fun BaseUi(
    showAlert: Boolean? = null,
    content: @Composable () -> Unit,
) {
    content.invoke()

    ShowAlert(
        showAlert = showAlert == true,
        isSuccess = true,
        title = "Mute clicked"
    )
}