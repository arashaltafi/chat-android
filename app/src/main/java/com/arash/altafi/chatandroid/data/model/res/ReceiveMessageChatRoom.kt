package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveMessageChatRoom(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("sendTime")
    val sendTime: Long? = null,
    @SerializedName("ownerId")
    val ownerId: Int? = null,
)