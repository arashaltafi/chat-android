package com.arash.altafi.chatandroid.data.model.res

import com.google.gson.annotations.SerializedName

data class ReceiveDeleteMessage(
    @SerializedName("messageId")
    val messageId: Long? = null,
)