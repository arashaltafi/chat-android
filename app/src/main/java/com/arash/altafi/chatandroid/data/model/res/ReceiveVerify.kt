package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveVerify(
    @SerializedName("message")
    val message: String,

    @SerializedName("token")
    val token: String,
)