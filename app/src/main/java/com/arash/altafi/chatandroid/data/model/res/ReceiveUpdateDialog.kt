package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveUpdateDialog(
    @SerializedName("dialog")
    val dialog: ReceiveDialogs? = null,
)