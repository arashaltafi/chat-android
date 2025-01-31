package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveUserStatusChatRoom(
    @SerializedName("usersCount")
    val usersCount: Int,
    @SerializedName("usersOnline")
    val usersOnline: Int,
)