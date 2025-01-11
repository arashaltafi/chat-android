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

data class ReceiveMessageChatRoom(
    @SerializedName("message")
    val message: ReceiveMessages,
)

data class ReceiveSendMessageChatRoom(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: ReceiveMessages,
)

data class ReceiveMessages(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("sendTime")
    val sendTime: Long? = null,
    @SerializedName("ownerId")
    val ownerId: Int? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("fullName")
    val fullName: String? = null,
)