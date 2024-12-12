package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveMessage(
    @SerializedName("message")
    val message: String,
)