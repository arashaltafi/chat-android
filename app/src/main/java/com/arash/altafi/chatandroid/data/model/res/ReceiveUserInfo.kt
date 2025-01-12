package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveUserInfo(
    @SerializedName("peerInfo")
    val peerInfo: UserPeerInfo
)

data class UserPeerInfo(
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
)