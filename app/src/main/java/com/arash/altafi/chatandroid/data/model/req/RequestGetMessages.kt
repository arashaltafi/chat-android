package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestGetMessages(
    @SerializedName("peerId")
    val peerId: Int?,
)