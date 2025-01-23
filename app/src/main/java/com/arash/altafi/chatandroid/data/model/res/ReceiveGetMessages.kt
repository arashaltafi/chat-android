package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveGetMessages(
    @SerializedName("messages")
    val messages: List<ReceiveMessagesPeer>,
    @SerializedName("peerInfo")
    val peerInfo: PeerInfo? = null,
)

data class ReceiveMessagesPeer(
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
)

data class PeerInfo(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("family")
    val family: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("lastSeen")
    val lastSeen: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("isBlock")
    val isBlock: Boolean? = null,
    @SerializedName("isBlockByMe")
    val isBlockByMe: Boolean? = null,
)