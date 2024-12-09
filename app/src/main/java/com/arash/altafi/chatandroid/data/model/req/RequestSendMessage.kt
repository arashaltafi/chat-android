package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestSendMessage(
    @SerializedName("peerId")
    val peerId: String?,

    @SerializedName("roomId")
    val roomId: String?,

    @SerializedName("text")
    val text: String?,
)