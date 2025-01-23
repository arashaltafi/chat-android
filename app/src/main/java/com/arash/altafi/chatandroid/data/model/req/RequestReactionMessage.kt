package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestReactionMessage(
    @SerializedName("peerId")
    val peerId: Int?,
    @SerializedName("messageId")
    val messageId: Long?,
    @SerializedName("reaction")
    val reaction: String?,
)