package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveReactionMessage(
    @SerializedName("peerId")
    val peerId: Int? = null,
    @SerializedName("messageId")
    val messageId: Long? = null,
    @SerializedName("reaction")
    val reaction: String? = null,
)