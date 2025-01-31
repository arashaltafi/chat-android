package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveSendMessage(
    @SerializedName("message")
    val messages: ReceiveMessagesPeer,
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("sendedSuccess")
    val sendedSuccess: Boolean? = null,
)