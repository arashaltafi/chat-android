package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveGetMessagesChatRoom(
    @SerializedName("messages")
    val messages: List<ReceiveMessages>,
    @SerializedName("usersCount")
    val usersCount: Int? = null,
    @SerializedName("usersOnline")
    val usersOnline: Int? = null,
)

data class ReceiveMessages(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("sendTime")
    val sendTime: Long? = null,
    @SerializedName("isSendedFromMe")
    val isSendedFromMe: Boolean? = null,
)