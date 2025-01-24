package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveBlockList(
    @SerializedName("blockList")
    val blockList: List<BlockList>,
)

data class BlockList(
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("lastSeen")
    val lastSeen: String? = null,
)