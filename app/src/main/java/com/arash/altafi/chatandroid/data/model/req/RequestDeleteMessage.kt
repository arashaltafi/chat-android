package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestDeleteMessage(
    @SerializedName("messageId")
    val messageId: Long? = null,

    @SerializedName("peerId")
    val peerId: Int? = null,

    @SerializedName("deleteBoth")
    val deleteBoth: Boolean? = null,
)