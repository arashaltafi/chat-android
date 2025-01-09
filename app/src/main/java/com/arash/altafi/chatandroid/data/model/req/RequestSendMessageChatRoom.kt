package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendMessageChatRoom(
    @SerializedName("text")
    val text: String? = null,

    @SerializedName("url")
    val url: String? = null,
)