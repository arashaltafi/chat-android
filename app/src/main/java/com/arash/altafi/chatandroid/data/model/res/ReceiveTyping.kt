package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveTyping(
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("message")
    val message: String? = null,
)