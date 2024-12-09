package com.arash.altafi.chatandroid.data.model.req

import com.google.gson.annotations.SerializedName

data class RequestGetMessages(
    @SerializedName("roomId")
    val roomId: String?,
)