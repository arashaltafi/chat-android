package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendMessage(
    @SerializedName("peerId")
    val peerId: Int?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("text")
    val text: String?,
)