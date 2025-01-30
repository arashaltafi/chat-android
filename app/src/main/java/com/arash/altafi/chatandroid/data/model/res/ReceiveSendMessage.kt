package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveSendMessage(
    @SerializedName("message")
    val messages: ReceiveSendMessagesPeer,
    @SerializedName("peerId")
    val peerId: Int? = null,
)

data class ReceiveSendMessagesPeer(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("sendTime")
    val sendTime: Long? = null,
    @SerializedName("seenTime")
    var seenTime: Long? = null,
    @SerializedName("deliverTime")
    var deliverTime: Long? = null,
    @SerializedName("isComing")
    val isComing: Boolean? = null,
    @SerializedName("reaction")
    val reaction: String? = null,
)