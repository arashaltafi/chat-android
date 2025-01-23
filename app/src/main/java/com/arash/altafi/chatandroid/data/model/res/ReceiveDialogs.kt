package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveDialog(
    @SerializedName("dialogs")
    val dialogs: List<ReceiveDialogs>,
)

data class ReceiveDialogs(
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("lastMessage")
    val lastMessage: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("time")
    val time: String? = null,
    @SerializedName("unreadCount")
    val unreadCount: Int? = null,
    @SerializedName("isTyping")
    val isTyping: String? = null,
    @SerializedName("isOnline")
    val isOnline: Boolean? = null,
)